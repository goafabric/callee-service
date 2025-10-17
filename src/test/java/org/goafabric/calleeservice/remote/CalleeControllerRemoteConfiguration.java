package org.goafabric.calleeservice.remote;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.test.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@Lazy
public class CalleeControllerRemoteConfiguration {

    @Bean
    public CalleeControllerRemote calleControllerRemote(RestClient.Builder builder,
                                                        @LocalServerPort String port, @Value("${adapter.timeout}") Long timeout) {
        return createAdapter(CalleeControllerRemote.class, builder, "http://localhost:" + port, timeout);
    }

    public static <A> A createAdapter(Class<A> adapterType, RestClient.Builder builder, String url, Long timeout) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(timeout.intValue());
        requestFactory.setReadTimeout(timeout.intValue());

        builder.baseUrl(url)
                .defaultHeaders(header -> header.setBasicAuth("admin", "admin"))
                .requestFactory(requestFactory);

        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(builder.build())).build()
                .createClient(adapterType);
    }

}


