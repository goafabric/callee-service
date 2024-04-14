[![Quality Gate Status](https://v2202402203466256255.megasrv.de/sonar/api/project_badges/measure?project=org.goafabric%3Acallee-service&metric=alert_status&token=sqb_946712941f7cfc15df1ac463e33dac7c0a2c3bc6)](https://v2202402203466256255.megasrv.de/sonar/dashboard?id=org.goafabric%3Acallee-service)
[![Maintainability Rating](https://v2202402203466256255.megasrv.de/sonar/api/project_badges/measure?project=org.goafabric%3Acallee-service&metric=sqale_rating&token=sqb_946712941f7cfc15df1ac463e33dac7c0a2c3bc6)](https://v2202402203466256255.megasrv.de/sonar/dashboard?id=org.goafabric%3Acallee-service)
[![Reliability Rating](https://v2202402203466256255.megasrv.de/sonar/api/project_badges/measure?project=org.goafabric%3Acallee-service&metric=reliability_rating&token=sqb_946712941f7cfc15df1ac463e33dac7c0a2c3bc6)](https://v2202402203466256255.megasrv.de/sonar/dashboard?id=org.goafabric%3Acallee-service)
[![Security Rating](https://v2202402203466256255.megasrv.de/sonar/api/project_badges/measure?project=org.goafabric%3Acallee-service&metric=security_rating&token=sqb_946712941f7cfc15df1ac463e33dac7c0a2c3bc6)](https://v2202402203466256255.megasrv.de/sonar/dashboard?id=org.goafabric%3Acallee-service)
[![Coverage](https://v2202402203466256255.megasrv.de/sonar/api/project_badges/measure?project=org.goafabric%3Acallee-service&metric=coverage&token=sqb_946712941f7cfc15df1ac463e33dac7c0a2c3bc6)](https://v2202402203466256255.megasrv.de/sonar/dashboard?id=org.goafabric%3Acallee-service)
# docker compose
go to /src/deploy/docker and do "./stack up" or "./stack up -native"

# run jvm multi image
docker run --pull always --name callee-service --rm -p50900:50900 goafabric/callee-service:$(grep '^version=' gradle.properties | cut -d'=' -f2)

# run native image
docker run --pull always --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:$(grep '^version=' gradle.properties | cut -d'=' -f2) -Xmx32m

# run native image arm
docker run --pull always --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native-arm64v8:$(grep '^version=' gradle.properties | cut -d'=' -f2) -Xmx32m