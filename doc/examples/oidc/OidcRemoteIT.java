package org.goafabric.calleeservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OidcRemoteIT {
    @Autowired
    private WebClient webClient;

    @Test
    void callMe() {
        //needs a non public oauth2-proxy client with credentials + service_account_roles checkbox ticked
        String name = webClient.get()
                .uri("http://localhost:50900/callees/sayMyName?name=Heisenberg")
                .retrieve()
                .bodyToMono(String.class).block();
    }

    @Bean
    ReactiveClientRegistrationRepository getRegistration(
    ) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("bael")
                .tokenUri("http://localhost:30200/oidc/realms/tenant-0/protocol/openid-connect/token")
                .clientId("oauth2-proxy")
                .clientSecret("S7Caqb5Rw999VlPPDuV9GGs3XYz834RK")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    public WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations) {
        InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("bael");
        return WebClient.builder()
                .filter(oauth)
                .build();

    }



}
