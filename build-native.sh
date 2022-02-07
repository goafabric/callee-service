#!/bin/bash
git pull
time mvn clean deploy -P docker-image-native
docker run --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:1.2.1
