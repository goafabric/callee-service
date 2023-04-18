package org.goafabric.personservice.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;

@Configuration
@TypeHint(types = org.goafabric.personservice.adapter.Callee.class)
@Slf4j
public class CalleeServiceConfiguration {

    @Bean
    public RestTemplate restTemplate(
            @Value("${adapter.calleeservice.user}") String user,
            @Value("${adapter.calleeservice.password}") String password,
            @Value("${adapter.timeout}") Integer timeout) {
        final RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(timeout))
                .setReadTimeout(Duration.ofMillis(timeout))
                .build();

        restTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken());
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    //todo: token refresh missing
    public String getAccessToken() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (OAuth2AuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return authorizedClientService.loadAuthorizedClient(
                    ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId(),
                    authentication.getName()
            ).getAccessToken().getTokenValue();
        } else if (JwtAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return ((AbstractOAuth2Token) authentication.getCredentials()).getTokenValue();
        } else {
            log.warn("Cannot obtain access token for logged in user");
            return "";
        }
    }

}
