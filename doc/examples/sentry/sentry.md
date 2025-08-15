# gradle
implementation("io.sentry:sentry-spring-boot-starter-jakarta:8.19.1"); implementation ("io.sentry:sentry-opentelemetry-agentless-spring:8.19.1")

# application
Sentry.init(options -> options.setDsn("https://YOUR_PUBLIC_KEY@o0.ingest.sentry.io/PROJECT_ID"));
