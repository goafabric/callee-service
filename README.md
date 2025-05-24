# sonarqube
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=org.goafabric%3Acallee-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=org.goafabric%3Acallee-service)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.goafabric%3Acallee-service&metric=coverage)](https://sonarcloud.io/summary/new_code?id=org.goafabric%3Acallee-service)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=org.goafabric%3Acallee-service&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=org.goafabric%3Acallee-service)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=org.goafabric%3Acallee-service&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=org.goafabric%3Acallee-service)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=org.goafabric%3Acallee-service&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=org.goafabric%3Acallee-service)


# docker compose
go to /src/deploy/docker and do "./stack up" or "./stack up -native"

# run jvm multi image
docker run --pull always --name callee-service --rm -p50900:50900 goafabric/callee-service:$(grep '^version=' gradle.properties | cut -d'=' -f2)

# run native image
docker run --pull always --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:$(grep '^version=' gradle.properties | cut -d'=' -f2) -Xmx32m

# run native image arm
docker run --pull always --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native-arm64v8:$(grep '^version=' gradle.properties | cut -d'=' -f2) -Xmx32m
                         
# native release tag
IMAGE=goafabric/callee-service-native-arm64v8; VERSION=$(grep '^version=' gradle.properties | cut -d'=' -f2); docker pull $IMAGE:$VERSION && docker tag $IMAGE:$VERSION $IMAGE:${VERSION%-SNAPSHOT} && docker push $IMAGE:${VERSION%-SNAPSHOT}