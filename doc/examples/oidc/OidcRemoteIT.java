package org.goafabric.calleeservice.adapter;

import org.goafabric.calleeservice.controller.Callee;
import org.junit.jupiter.api.Test;
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

//testImplementation("org.springframework.boot:spring-boot-starter-webflux")

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OidcRemoteIT {
    @Test
    void callMe() {
        Callee callee = webClient(clientRegistrations()).get() //normally beans inside configuration class and then webclient injected
                .uri("http://localhost:50900/callees/sayMyName?name=Heisenberg")
                .retrieve()
                .bodyToMono(Callee.class).block();
        //System.out.println(callee); //currently returns null
    }


    @Bean
    ReactiveClientRegistrationRepository clientRegistrations() {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("bael")
                .tokenUri("http://localhost:30200/oidc/token")
                .clientId("oauth2-proxy")
                .clientSecret("none")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    public WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations) {
        var authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                clientRegistrations, new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations));
        var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("bael");
        return WebClient.builder()
                .filter(oauth)
                .build();

    }
}


