//package org.goafabric.calleeservice.kubernetes;
//import io.fabric8.kubernetes.client.KubernetesClient;
//import io.fabric8.kubernetes.client.KubernetesClientBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.stream.Stream;
//
//import static org.goafabric.calleeservice.kubernetes.ProvisionUtil.*;
//
////implementation("io.fabric8:kubernetes-client:7.3.1")
////TODO: running inside kubernetes + registry update
//@Component
//public class ProvisionLogic implements CommandLineRunner {
//    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
//
//    @Value("${provision.namespaces:example,core,billing}")
//    private String namespaces;
//
//    @Value("${provision.tenants:0}")
//    private String tenantIds;
//
//    @Value("${provision.maxUpdatePods:8}")
//    private Integer maxUpdatePods;
//
//    @Value("${provision.mode:create}")
//    private String mode;
//
//    @Value("${provision.inMemory:false}")
//    private Boolean inMemory;
//
//    @Override
//    public void run(String... args) throws Exception {
//        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
//            var deployments = searchDeploymentsForJdbc(client, this.namespaces);
//            switch (mode) {
//                case "create" -> create(client, deployments);
//                case "update" -> update(client, deployments);
//                default -> throw new IllegalStateException("unknown mode");
//            }
//        }  catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void create(KubernetesClient client, List<ProvisionUtil.DeploymentSpecification> deployments) {
//        Stream.of(tenantIds.split(",")).forEach(tenantId -> {
//            log.info("processing tenant {}", tenantId);
//            deployments.forEach(deploy -> {
//                log.info("schema creation for tenant {} and app {}", tenantId, deploy.name());
//                createPod(client, deploy, tenantId, inMemory);
//            });
//        });
//
//        log.info("all creations finished successfully");
//    }
//
//    public void update(KubernetesClient client, List<ProvisionUtil.DeploymentSpecification> deployments) {
//        deployments.forEach(deploy -> {
//            log.info("schema update for tenants {} and app {}", tenantIds, deploy.name());
//            scaleTo(client, deploy.nameSpace(), deploy.name(), 0);
//
//            var futures = splitIntoGroups(tenantIds, maxUpdatePods).stream()
//                    .map(groupCsv -> createPodAsync(client, deploy, groupCsv, inMemory))
//                    .toList().toArray(new CompletableFuture[0]);
//
//            CompletableFuture.allOf(futures).join(); // wait to finish
//
//            scaleTo(client, deploy.nameSpace(), deploy.name(), deploy.replicas());
//        });
//
//        log.info("all updates finished successfully");
//    }
//
//    private static void scaleTo(KubernetesClient client, String nameSpace, String name, Integer replicas) {
//        client.apps().deployments().inNamespace(nameSpace).withName(name)
//                .scale(replicas);
//    }
//
//}
