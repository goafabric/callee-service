package org.goafabric.calleeservice.declarative;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
@ImportRuntimeHints(AdapterConfiguration.AdapterRuntimeHints.class)
public class AdapterConfiguration {

    @Bean
    public CalleeServiceAdapterDec calleeServiceAdapterDec(//ReactorLoadBalancerExchangeFilterFunction lbFunction,
                                                     WebClient.Builder builder,
            @Value("${adapter.calleeservice.url}") String url, @Value("${adapter.timeout}") Long timeout, @Value("${adapter.maxlifetime:-1}") Long maxLifeTime) {
        return createAdapter(CalleeServiceAdapterDec.class, builder, url, timeout, maxLifeTime);
    }

    public static <A> A createAdapter(Class<A> adapterType, WebClient.Builder builder, String url, Long timeout, Long maxLifeTime) {
        builder.baseUrl(url)
                .defaultHeaders(header -> header.setBasicAuth("admin", "admin"))
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.builder("custom").maxLifeTime(Duration.ofMillis(maxLifeTime)).build())));

        return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(builder.build())).blockTimeout(Duration.ofMillis(timeout))
                .build().createClient(adapterType);
    }

    static class AdapterRuntimeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(io.github.resilience4j.spring6.circuitbreaker.configure.CircuitBreakerAspect.class,
                    builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_METHODS));
        }
    }
}


