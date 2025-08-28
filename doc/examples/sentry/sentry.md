# gradle
implementation("io.sentry:sentry:8.19.1");
//implementation("io.sentry:sentry-spring-boot-starter-jakarta:8.19.1"); implementation ("io.sentry:sentry-opentelemetry-agentless-spring:8.19.1")


# application.yaml
sentry:
  dsn: https://5318f5c32dce4a04e10522fec0d3bf5c@o4509847473684480.ingest.de.sentry.io/4509847475978320
  send-default-pii: true
  traces-sample-rate: 1.0

# results
- simple sentry dependency without spring boot
  - will just create Exceptions
  - compatible with native + Spring Boot 4.0 M1
- sentry + spring boot + opentelemetry
  - will also create traces (exactly the same ones as in tempo)
  - compatible with native, but currently incomaptible with Spring Boot 4.0 M1
  - tracing has a bad bug, when an error is thrown the request will hang forever (receiver class io.opentelemetry.sdk.logs.ExtendedSdkLogRecordBuilder does not define or inherit an implementation of the resolved method 'abstract io.opentelemetry.api.incubator.logs.ExtendedLogRecordBuilder)
- question
  - what is the value over traces in tempo, and exceptions in loki ?

# collector (not working)

https://opentelemetry.io/docs/collector/quick-start/
https://github.com/open-telemetry/opentelemetry-collector-contrib/blob/main/exporter/sentryexporter/README.md

docker run --rm \
-v $(pwd)/config.yaml:/etc/otelcol-contrib/config.yaml \
-p 4317:4317 \
-p 4318:4318 \
-p 55679:55679 \
otel/opentelemetry-collector-contrib:0.133.0

# jaeger
docker run --rm --name jaeger -e COLLECTOR_OTLP_ENABLED=true -p 43180:4318 -p 16686:16686  jaegertracing/all-in-one:1.51.0 && open http://localhost:16686