package org.goafabric.calleeservice.remote

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpHeaders
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
@Lazy
class AdapterConfiguration {
    @Bean
    fun calleControllerRemote( //ReactorLoadBalancerExchangeFilterFunction lbFunction,
        builder: RestClient.Builder,
        @LocalServerPort port: String,
        @Value("\${adapter.timeout}") timeout: Long,
        @Value("\${adapter.maxlifetime:-1}") maxLifeTime: Long?
    ): CalleeControllerRemote {
        return createAdapter(
            CalleeControllerRemote::class.java, builder, "http://localhost:$port", timeout, maxLifeTime
        )
    }

    companion object {
        fun <A> createAdapter(
            adapterType: Class<A>,
            builder: RestClient.Builder,
            url: String,
            timeout: Long,
            maxLifeTime: Long?
        ): A {
            val requestFactory = SimpleClientHttpRequestFactory()
            requestFactory.setConnectTimeout(timeout.toInt())
            requestFactory.setReadTimeout(timeout.toInt())

            builder.baseUrl(url)
                .defaultHeaders { header: HttpHeaders -> header.setBasicAuth("admin", "admin") }
                .requestFactory(requestFactory)

            //.clientConnector(new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.builder("custom").maxLifeTime(Duration.ofMillis(maxLifeTime)).build())));
            return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(builder.build())).build()
                .createClient(adapterType)
        }
    }
}


