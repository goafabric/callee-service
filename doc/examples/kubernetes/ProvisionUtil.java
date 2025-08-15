package org.goafabric.calleeservice.kubernetes;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ProvisionUtil {
    private static final Logger log = LoggerFactory.getLogger(ProvisionUtil.class.getName());

    public record DeploymentSpecification(String nameSpace, String name, String image, String dataSource,
                                          List<SecretEnvSource> secretEnvs, Integer replicas) {
    }

    public static CompletableFuture<Void> createPodAsync(KubernetesClient client, DeploymentSpecification deployment, String tenantId, boolean inMemory) {
        return CompletableFuture.runAsync(() -> createPod(client, deployment, tenantId, inMemory));
    }

    public static void createPod(KubernetesClient client, DeploymentSpecification deployment, String tenantId, boolean inMemory) {
        String podName = deployment.name() + "-provision-" + UUID.randomUUID().toString().substring(0, 8);

        client.pods().inNamespace(deployment.nameSpace).withName(podName).delete();

        Pod pod = new PodBuilder()
                .withNewMetadata().withName(podName).endMetadata().withNewSpec()
                .withRestartPolicy("Never")
                .addNewContainer().withName(podName).withImage(deployment.image)
                .withEnv(
                        new EnvVar("database.provisioning.goals", "-migrate -terminate", null),
                        new EnvVar("multi-tenancy.tenants", tenantId, null),
                        !inMemory ? new EnvVar("spring.datasource.url", deployment.dataSource(), null) : new EnvVar("dummy", "dummy", null)
                )
                .withEnvFrom(getSecrets(deployment))

                .endContainer()
                .endSpec()
                .build();

        client.pods().inNamespace(deployment.nameSpace).resource(pod).create();

        waitToFinish(deployment.nameSpace, client, podName, tenantId);
    }

    get secrets to be attached, this only works for secrets refs, not for CSI Driver like Volume Secrets
    private static List<EnvFromSource> getSecrets(DeploymentSpecification deployment) {
        return deployment.secretEnvs.stream()
                .map(secretEnvSource -> new EnvFromSourceBuilder()
                        .withSecretRef(secretEnvSource)
                        .build())
                .toList();
    }

    private static void waitToFinish(String nameSpace, KubernetesClient client, String podName, String tenantId) {
        client.pods()
                .inNamespace(nameSpace)
                .withName(podName)
                .waitUntilCondition(
                        p -> {
                            if (p == null || p.getStatus() == null) return false;
                            String phase = p.getStatus().getPhase();
                            log.info("Phase: {} Tenant: {} Pod: {} ", phase, tenantId, podName);
                            if ("Failed".equals(phase)) {
                                throw new IllegalStateException("Pods failed");
                            }
                            return "Succeeded".equals(phase);
                        },
                        60, TimeUnit.SECONDS
                );
        ;
    }

    static List<DeploymentSpecification> searchDeploymentsForJdbc(KubernetesClient client, String namespaces) {
        deleteCompletedPods(client, namespaces);

        var deployments = new ArrayList<DeploymentSpecification>();
        var configMap = new AtomicReference<ConfigMap>();

        List.of(namespaces.split(",")).forEach(ns -> {
            client.apps().deployments().inNamespace(ns).list().getItems()
                    .forEach(deployment -> {
                        deployment.getSpec().getTemplate().getSpec().getContainers().stream()
                                .filter(container -> container.getEnvFrom().stream()
                                        .map(EnvFromSource::getConfigMapRef)
                                        .filter(ref -> ref != null && ref.getName() != null)
                                        .anyMatch(ref -> {
                                            ConfigMap cm = client.configMaps().inNamespace(ns).withName(ref.getName()).get();
                                            configMap.set(cm);
                                            return cm != null && cm.getData() != null && cm.getData().containsKey("spring.datasource.url");
                                        })
                                )
                                .forEach(container -> createDeploymentSpec(deployment, container, deployments, configMap));
                    });
        });

        deployments.forEach(deploy -> log.info("deployments detected: " + deploy));
        return deployments;
    }

    private static void createDeploymentSpec(Deployment deployment, Container container, List<DeploymentSpecification> deployments, AtomicReference<ConfigMap> configMap) {
        deployments.add(new DeploymentSpecification(deployment.getMetadata().getNamespace(),
                deployment.getMetadata().getName(), container.getImage(),
                configMap.get().getData().get("spring.datasource.url"),
                getSecretsFromContainer(container),
                deployment.getSpec().getReplicas()));
    }

    private static List<SecretEnvSource> getSecretsFromContainer(Container container) {
        return container.getEnvFrom().stream().map(EnvFromSource::getSecretRef)
                .filter(ref -> ref != null && ref.getName() != null)
                .toList();
    }

    public static void deleteCompletedPods(KubernetesClient client, String namespaces) {
        Stream.of(namespaces.split(",")).forEach(namespace -> {
            client.pods()
                    .inNamespace(namespace).list().getItems().stream()
                    .filter(pod -> ("Succeeded".equalsIgnoreCase(pod.getStatus().getPhase()) || "Failed".equalsIgnoreCase(pod.getStatus().getPhase()))).toList()
                    .forEach(pod -> {
                        String podName = pod.getMetadata().getName();
                        client.pods().inNamespace(namespace).withName(podName).delete();
                    });
        });
    }


    static List<String> splitIntoGroups(String tenants, int maxUpdatePods) {
        List<String> tenantIds = Arrays.asList(tenants.split(","));
        int groupSize = (int) Math.ceil((double) tenantIds.size() / maxUpdatePods);

        List<String> groups = new ArrayList<>();
        for (int i = 0; i < tenantIds.size(); i += groupSize) {
            List<String> group = tenantIds.subList(i, Math.min(i + groupSize, tenantIds.size()));
            groups.add(String.join(",", group));
        }
        return groups;
    }

}
