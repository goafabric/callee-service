package org.goafabric.calleeservice.remote;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@Lazy
public class AdapterConfiguration {

    @Bean
    public CalleeControllerRemote calleControllerRemote(//ReactorLoadBalancerExchangeFilterFunction lbFunction,
                                                       RestClient.Builder builder,
                                                       @LocalServerPort String port, @Value("${adapter.timeout}") Long timeout, @Value("${adapter.maxlifetime:-1}") Long maxLifeTime) {
        return createAdapter(CalleeControllerRemote.class, builder, "http://localhost:" + port, timeout, maxLifeTime);
    }

    public static <A> A createAdapter(Class<A> adapterType, RestClient.Builder builder, String url, Long timeout, Long maxLifeTime) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(timeout.intValue());
        requestFactory.setReadTimeout(timeout.intValue());

        builder.baseUrl(url)
                .defaultHeaders(header -> header.setBasicAuth("admin", "admin"))
                .requestFactory(requestFactory);
                //.clientConnector(new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.builder("custom").maxLifeTime(Duration.ofMillis(maxLifeTime)).build())));

        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(builder.build())).build()
                .createClient(adapterType);
    }

}


