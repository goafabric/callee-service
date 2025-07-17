package org.goafabric.calleeservice.kubernetes;

import io.fabric8.kubernetes.api.model.*;
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

    public record DeploymentSpecification(String nameSpace, String name, String image, String dataSource, Integer replicas) {}

    public static CompletableFuture<Void> createPodAsync(KubernetesClient client, DeploymentSpecification deployment, String tenantId) {
        return CompletableFuture.runAsync(() -> createPod(client, deployment, tenantId));
    }

    public static void createPod(KubernetesClient client, DeploymentSpecification deployment, String tenantId) {
        createPod(client, deployment.nameSpace(), deployment.name(), deployment.image(), tenantId);
    }

    private static void createPod(KubernetesClient client, String nameSpace, String name, String imageName, String tenantId) {
        String podName = name + "-provision-" + UUID.randomUUID().toString().substring(0, 8);

        client.pods().inNamespace(nameSpace).withName(podName).delete();

        Pod pod = new PodBuilder()
                .withNewMetadata().withName(podName).endMetadata().withNewSpec()
                .withRestartPolicy("Never")
                .addNewContainer().withName(podName).withImage(imageName)
                .withEnv(
                        new EnvVar("database.provisioning.goals", "-migrate -terminate", null),
                        new EnvVar("multi-tenancy.tenants", tenantId, null)
                )
                .endContainer()
                .endSpec()
                .build();

        client.pods().inNamespace(nameSpace).resource(pod).create();

        waitToFinish(nameSpace, client, podName, tenantId);
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
                        30, TimeUnit.SECONDS
                );;
    }

    static List<DeploymentSpecification> searchDeploymentsForJdbc(KubernetesClient client, String namespaces) {
        deleteCompletedPods(client, namespaces);

        List<DeploymentSpecification> deployments = new ArrayList<>();

        AtomicReference<ConfigMap> configMap = new AtomicReference<>();

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
                                .forEach(container -> {
                                            List<SecretEnvSource> lst = container.getEnvFrom().stream()
                                                    .map(EnvFromSource::getSecretRef)
                                                    .filter(ref -> ref != null && ref.getName() != null)
                                                    .toList();
                                            deployments.add(new DeploymentSpecification(deployment.getMetadata().getNamespace(),
                                                    deployment.getMetadata().getName(), container.getImage(),
                                                    configMap.get().getData().get("spring.datasource.url"),
                                                    deployment.getSpec().getReplicas()));
                                        }
                                );
                    });
        });

        deployments.forEach(deploy -> log.info("deployments detected: " + deploy));
        return deployments;
    }

    public static void deleteCompletedPods(KubernetesClient client, String namespaces) {
        Stream.of(namespaces.split(",")).forEach(namespace -> {
            client.pods()
                    .inNamespace(namespace).list().getItems().stream()
                    .filter(pod -> "Succeeded".equalsIgnoreCase(pod.getStatus().getPhase())).toList()
                    .forEach(pod -> {
                        String podName = pod.getMetadata().getName();
                        client.pods().inNamespace(namespace).withName(podName).delete();
            });
        });
    }


    static List<String> splitIntoGroupsAsCsv(String tenants, int maxUpdatePods) {
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
