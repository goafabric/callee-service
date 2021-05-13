#!/bin/bash
git pull
time mvn clean spring-boot:build-image -P docker-image-native
#time mvn clean spring-boot:build-image install -P docker-image-native
docker run --name callee-service-native --rm -p50900:50900 goafabric/calle-service-native:1.0.2-SNAPSHOT

#docker pull goafabric/calle-service-native:1.0.2-SNAPSHOT && docker run --name callee-service-native --rm -p50900:50900 goafabric/calle-service-native:1.0.2-SNAPSHOT -Xmx64m

