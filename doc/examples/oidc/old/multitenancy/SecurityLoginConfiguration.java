package org.goafabric.calleeservice.crossfunctional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SecurityLoginConfiguration {
    @Value("${spring.security.auth-server-url}") private String authBaseUri;
    @Value("${spring.security.client-id}") private String clientId;

    //Frontend Login
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new DynamicRepository();
    }

    private class DynamicRepository implements ClientRegistrationRepository, Iterable<ClientRegistration> {
        final ConcurrentHashMap<String, ClientRegistration> clientRegistrations = new ConcurrentHashMap();

        @Override
        public Iterator<ClientRegistration> iterator() {
            return Collections.singletonList(ClientRegistration.withRegistrationId("oidc").clientId(clientId).authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri("#").authorizationUri("#").tokenUri("#").userInfoUri("#").build()).iterator();
            //return Collections.singletonList(createClientRegistration("")).iterator(); //only needed for scanning the redirectUri duing Oauth2LoginConfigurer.getLoginLinks()
        }

        @Override
        public ClientRegistration findByRegistrationId(String registrationId) {
            return clientRegistrations.computeIfAbsent(HttpInterceptor.getTenantId(), (k) -> createClientRegistration(HttpInterceptor.getTenantId()));
        }
    }

    private ClientRegistration createClientRegistration(String tenantId) {
        return ClientRegistration.withRegistrationId("oidc")
                .clientId(clientId)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userNameAttributeName("preferred_username")
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationUri(authBaseUri + tenantId + "/protocol/openid-connect/auth")
                .tokenUri(authBaseUri + tenantId + "/protocol/openid-connect/token")
                .userInfoUri(authBaseUri + tenantId + "/protocol/openid-connect/userinfo")
                .build();
    }

    //JWT Backend2Backend
    @Slf4j
    @RequiredArgsConstructor
    static class MyJwtAuthenticationManagerResolver implements AuthenticationManagerResolver<String> {
        private final String authBaseUri;
        private final Map<String, AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();

        @Override
        public AuthenticationManager resolve(String issuer) {
            if (issuer.startsWith(authBaseUri)) {  //this is only to check the validity of the issuer url, instead if having one per tenant, we just scan the base url
                return this.authenticationManagers.computeIfAbsent(issuer,
                        (k) -> new JwtAuthenticationProvider(JwtDecoders.fromIssuerLocation(issuer))::authenticate);
            }
            return null;
        }
    }
}
