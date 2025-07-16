package org.goafabric.calleeservice.kubernetes;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


//TODO: seperate create + update, autodetection for spring boot apps and put config into hashmap
@Component
public class ProvisionLogic implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${namespaces:example,core}")
    private String namespaces;

    @Value("${multi-tenancy.tenants:0}")
    private String tenantIds;

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        //execute();
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            var deployments = searchDeployments2(client);
            create(client, deployments);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void create(KubernetesClient client, List<DeploymentSpec> deployments) {
        List.of(tenantIds.split(",")).stream().forEach(tenantId -> {
            log.info("processing tenant {}", tenantId);
            deployments.forEach(deploy -> {
                log.info("creating ... {}", deploy.name);
                createPod(client, deploy.nameSpace, deploy.image, tenantId);
            });
        });

        log.info("create finished successfully");
    }


    private void createPod(KubernetesClient client, String nameSpace, String imageName, String tenantId) {
        String podName = imageName.split(":")[0].split("/")[1] + "-provision";

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

    private void waitToFinish(String nameSpace, KubernetesClient client, String podName) {
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


    private Integer scaleTo(KubernetesClient client, String namespace, String imageName, Integer replicaCount, boolean update) {
        if (!update) {
            return -1;
        }
        var deployment = findDeploymentByImage(client, namespace, imageName);

        Integer replicas = deployment.getSpec().getReplicas();

        log.info("scaling to {}" , replicaCount);
        // Scale it to 0
        client.apps()
                .deployments()
                .inNamespace(namespace)
                .withName(deployment.getMetadata().getName())
                .scale(replicaCount);
        return replicas;
    }


    private Deployment findDeploymentByImage(KubernetesClient client, String namespace, String imageName) {
        return client.apps()
                .deployments()
                .inNamespace(namespace)
                .list()
                .getItems()
                .stream()
                .filter(deployment ->
                        deployment.getSpec().getTemplate().getSpec().getContainers().stream()
                                .anyMatch(container -> container.getImage().equals(imageName)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No deployment found with image: " + imageName));
    }

    private List<DeploymentSpec> searchDeployments2(KubernetesClient client) {
        List<DeploymentSpec> deployments = new ArrayList<>();

        List.of(this.namespaces.split(",")).forEach(ns -> {
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
                                        deployments.add(new DeploymentSpec(deployment.getMetadata().getNamespace(), deployment.getMetadata().getName(), container.getImage()))
                                );
                    });
        });
        deployments.stream().forEach(deploy -> {
            log.info("deployments detected: " + deploy);
        });
        return deployments;
    }



    private record DeploymentSpec(String nameSpace, String name, String image) {}
}
