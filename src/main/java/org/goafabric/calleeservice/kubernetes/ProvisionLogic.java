package org.goafabric.calleeservice.kubernetes;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ProvisionLogic implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${application.images:goafabric/core-service-native:3.5.0,goafabric/person-service-native:3.5.0}")
    private String applicationImages;

    @Autowired
    private ApplicationContext context;


    @Override
    public void run(String... args) throws Exception {
        execute();
    }


    public void execute() {
        boolean async = false;
        String nameSpace = "example";
        List<String> tenantIds = List.of("0", "5");

        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            tenantIds.forEach(tenantId -> {
                log.info("processing tenant {}", tenantId);
                Arrays.asList(applicationImages.split(",")).forEach(
                        imageName -> createPod(client, false, nameSpace, imageName, tenantId));
            });
        }
    }

    private void createPod(KubernetesClient client, boolean async, String nameSpace, String imageName, String tenantId) {
        log.info("executing ... {}", imageName);

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
                )
                .endContainer()
                .endSpec()
                .build();

        // Create the Pod
        client.pods().inNamespace(nameSpace).create(pod);

        if (!async) {
            waitToFinish(nameSpace, client, podName);
        }

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
                            return "Succeeded".equals(phase) || "Failed".equals(phase);
                        },
                        30, TimeUnit.SECONDS
                );
    }

}
