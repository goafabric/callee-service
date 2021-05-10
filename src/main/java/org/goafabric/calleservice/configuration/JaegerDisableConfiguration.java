/*
package org.goafabric.calleservice.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@ConditionalOnProperty(value = "opentracing.jaeger.disabled", havingValue = "true", matchIfMissing = true)
@Configuration
@Slf4j
public class JaegerDisableConfiguration {

    @Bean
    public io.opentracing.Tracer jaegerTracer() {
        log.info("Jaeger disabled");
        return io.opentracing.noop.NoopTracerFactory.create();
    }
}

 */