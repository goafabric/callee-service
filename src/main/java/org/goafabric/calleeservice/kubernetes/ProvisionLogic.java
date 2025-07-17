package org.goafabric.calleeservice.kubernetes;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.goafabric.calleeservice.kubernetes.ProvisionUtil.*;


//TODO: Connect to real database with secrets
@Component
public class ProvisionLogic implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${namespaces:example,core,billing}")
    private String namespaces;


    @Value("${multi-tenancy.tenants:0,5}")
    private String tenantIds;

    @Value("${max.update.pods:5}")
    private Integer maxUpdatePods;

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            var deployments = searchDeploymentsForJdbc(client, this.namespaces);
            //create(client, deployments);

            tenantIds = IntStream.range(0, 100).mapToObj(String::valueOf).collect(Collectors.joining(","));
            update(client, deployments);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(KubernetesClient client, List<ProvisionUtil.DeploymentSpecification> deployments) {
        Stream.of(tenantIds.split(",")).forEach(tenantId -> {
            log.info("processing tenant {}", tenantId);
            deployments.forEach(deploy -> {
                log.info("schema creation for tenant {} and app {}", tenantId, deploy.name());
                createPod(client, deploy, tenantId);
            });
        });

        log.info("all creations finished successfully");
    }

    public void update(KubernetesClient client, List<ProvisionUtil.DeploymentSpecification> deployments) {

        deployments.forEach(deploy -> {
            log.info("schema update for tenants {} and app {}", tenantIds, deploy.name());
            scaleTo(client, deploy.nameSpace(), deploy.name(), 0);

            var futures = splitIntoGroupsAsCsv(tenantIds, maxUpdatePods).stream()
                    .map(groupCsv -> createPodAsync(client, deploy, groupCsv))
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            scaleTo(client, deploy.nameSpace(), deploy.name(), deploy.replicas());
        });

        log.info("all updates finished successfully");
    }

    private static void scaleTo(KubernetesClient client, String nameSpace, String name, Integer replicas) {
        client.apps().deployments().inNamespace(nameSpace).withName(name)
                .scale(replicas);
    }



}
