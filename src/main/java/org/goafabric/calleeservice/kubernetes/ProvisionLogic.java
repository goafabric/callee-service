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

import static org.goafabric.calleeservice.kubernetes.ProvisionUtil.createPod;
import static org.goafabric.calleeservice.kubernetes.ProvisionUtil.searchDeployments2;


//TODO: seperate create + update, autodetection for spring boot apps and put config into hashmap
@Component
public class ProvisionLogic implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${namespaces:example,core,billing}")
    private String namespaces;

    @Value("${multi-tenancy.tenants:0}")
    private String tenantIds;

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        //execute();
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            var deployments = searchDeployments2(client, this.namespaces);
            create(client, deployments);
            //update(client, deployments);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void create(KubernetesClient client, List<ProvisionUtil.DeploymentSpecification> deployments) {
        List.of(tenantIds.split(",")).stream().forEach(tenantId -> {
            log.info("processing tenant {}", tenantId);
            deployments.forEach(deploy -> {
                log.info("creating ... {}", deploy.name());
                createPod(client, deploy.nameSpace(), deploy.name(), deploy.image(), tenantId);
            });
        });

        log.info("create finished successfully");
    }

    public void update(KubernetesClient client, List<ProvisionUtil.DeploymentSpecification> deployments) {
        deployments.forEach(deploy -> {
            log.info("updating with all tenants ... {}", deploy.name());
            scaleTo(client, deploy.nameSpace(), deploy.name(), 0);
            createPod(client, deploy.nameSpace(), deploy.name(), deploy.image(), tenantIds);
            scaleTo(client, deploy.nameSpace(), deploy.name(), deploy.replicas());
        });

        log.info("update finished successfully");
    }

    private static void scaleTo(KubernetesClient client, String nameSpace, String name, Integer replicas) {
        client.apps().deployments().inNamespace(nameSpace).withName(name)
                .scale(replicas);
    }



}
