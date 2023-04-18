package org.goafabric.calleeservice.crossfunctional;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MTKeycloakSpringBootConfigResolver implements KeycloakConfigResolver {
    @Autowired
    private AdapterConfig adapterConfig;

    @Override
    public KeycloakDeployment resolve(HttpFacade.Request request) {
        final KeycloakDeployment deployment = KeycloakDeploymentBuilder.build(this.adapterConfig);
        deployment.setRealm("tenant-" + getTenantId(request.getHeader("X-TenantId")));
        return deployment;
    }

    private String getTenantId(String tenantId) {
        return tenantId == null ? "0" : tenantId;  //Todo: should throw exception
    }
}
