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