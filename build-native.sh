#!/bin/bash
git pull
time mvn clean -P docker-image-native
docker run --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:1.1.0-SNAPSHOT
