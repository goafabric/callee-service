package org.goafabric.calleeservice.kubernetes;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProvisionUtil {
    private static final Logger log = LoggerFactory.getLogger(ProvisionUtil.class.getName());

    public record DeploymentSpecification(String nameSpace, String name, String image, Integer replicas) {}

    public static void createPod(KubernetesClient client, String nameSpace, String name, String imageName, String tenantId) {
        String podName = name + "-provision";

        // Delete existing Pod if it exists
        client.pods().inNamespace(nameSpace).withName(podName).delete();

        Pod pod = new PodBuilder()
                .withNewMetadata()
                .withName(podName)
                .endMetadata()
                .withNewSpec()
                .withRestartPolicy("Never")
                .addNewContainer()
                .withName(podName)
                .withImage(imageName)
                .withEnv(
                        new EnvVar("database.provisioning.goals", "-migrate -terminate", null),
                        new EnvVar("multi-tenancy.tenants", tenantId, null)
                        //,new EnvVar("spring.datasource.url", "jdbc:wrong", null)
                )
                .endContainer()
                .endSpec()
                .build();

        // Create the Pod
        client.pods().inNamespace(nameSpace).create(pod);

        waitToFinish(nameSpace, client, podName);
    }

    private static void waitToFinish(String nameSpace, KubernetesClient client, String podName) {
        client.pods()
                .inNamespace(nameSpace)
                .withName(podName)
                .waitUntilCondition(
                        p -> {
                            if (p == null || p.getStatus() == null) return false;
                            String phase = p.getStatus().getPhase();
                            log.info("Pod phase: {}", phase);
                            if ("Failed".equals(phase)) {
                                throw new IllegalStateException("Pods failed");
                            }
                            return "Succeeded".equals(phase);
                        },
                        30, TimeUnit.SECONDS
                );
    }


    static List<DeploymentSpecification> searchDeployments2(KubernetesClient client, String namespaces) {
        List<DeploymentSpecification> deployments = new ArrayList<>();

        List.of(namespaces.split(",")).forEach(ns -> {
            client.apps().deployments().inNamespace(ns).list().getItems()
                    .forEach(deployment -> {
                        deployment.getSpec().getTemplate().getSpec().getContainers().stream()
                                .filter(container -> container.getEnvFrom().stream()
                                        .map(EnvFromSource::getConfigMapRef)
                                        .filter(ref -> ref != null && ref.getName() != null)
                                        .anyMatch(ref -> {
                                            ConfigMap cm = client.configMaps().inNamespace(ns).withName(ref.getName()).get();
                                            return cm != null && cm.getData() != null && cm.getData().containsKey("spring.datasource.url");
                                        })
                                )
                                .forEach(container ->
                                        deployments.add(new DeploymentSpecification(deployment.getMetadata().getNamespace(), deployment.getMetadata().getName(), container.getImage(), deployment.getSpec().getReplicas()))
                                );
                    });
        });
        deployments.stream().forEach(deploy -> {
            log.info("deployments detected: " + deploy);
        });
        return deployments;
    }


}
