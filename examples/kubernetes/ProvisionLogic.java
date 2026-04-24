package org.goafabric.calleeservice.kubernetes;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.goafabric.calleeservice.kubernetes.ProvisionUtil.*;

//gradle
//implementation("io.fabric8:kubernetes-client:7.3.1")
//implementation("org.springframework.boot:spring-boot-starter-web"); implementation("org.springframework.boot:spring-boot-starter-actuator")
@RestController
public class ProvisionLogic implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${provision.namespaces:example,core,billing}")
    private String namespaces;

    @Value("${provision.tenants:0}")
    private String tenantIds;

    @Value("${provision.maxUpdatePods:8}")
    private Integer maxUpdatePods;

    @Value("${provision.mode:create}")
    private String mode;

    @Value("${provision.inMemory:false}")
    private Boolean inMemory;

    private final StringBuffer logMessage = new StringBuffer();

    @Override
    public void run(String... args) throws Exception {
        //init();
    }

    @GetMapping("/prov")
    public String provision() {
        logMessage.delete(0, logMessage.length());
        init();
        return logMessage.toString();
    }

    private void init() {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            var deployments = searchDeploymentsForJdbc(client, this.namespaces);
            switch (mode) {
                case "create" -> create(client, deployments);
                case "update" -> update(client, deployments);
                default -> throw new IllegalStateException("unknown mode");
            }
        }
    }

    public void create(KubernetesClient client, List<ProvisionUtil.DeploymentSpecification> deployments) {
        Stream.of(tenantIds.split(",")).forEach(tenantId -> {
            log.info("processing tenant {}", tenantId);
            logMessage.append("processing tenant ").append(tenantId).append("<p>");

            deployments.forEach(deploy -> {
                log.info("schema creation for tenant {} and app {}", tenantId, deploy.name());
                logMessage.append("schema creation for tenant ").append(tenantId).append(" and app ").append(deploy.name()).append("<p>");;

                createPod(client, deploy, tenantId, inMemory);
            });
        });

        log.info("all creations finished successfully");
        logMessage.append("all creations finished successfully").append("<p>");
    }

    public void update(KubernetesClient client, List<ProvisionUtil.DeploymentSpecification> deployments) {
        deployments.forEach(deploy -> {
            log.info("schema update for tenants {} and app {}", tenantIds, deploy.name());
            logMessage.append("schema update for tenants ").append(tenantIds).append(" and app ").append(deploy.name()).append("<p>");

            scaleTo(client, deploy.nameSpace(), deploy.name(), 0);

            var futures = splitIntoGroups(tenantIds, maxUpdatePods).stream()
                    .map(groupCsv -> createPodAsync(client, deploy, groupCsv, inMemory))
                    .toList().toArray(new CompletableFuture[0]);

            CompletableFuture.allOf(futures).join(); // wait to finish

            scaleTo(client, deploy.nameSpace(), deploy.name(), deploy.replicas());
        });

        log.info("all updates finished successfully");
        logMessage.append("all updates successfully").append("<p>");
    }

    private static void scaleTo(KubernetesClient client, String nameSpace, String name, Integer replicas) {
        client.apps().deployments().inNamespace(nameSpace).withName(name)
                .scale(replicas);
    }

}
