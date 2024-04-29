# multiarch native
docker manifest create goafabric/callee-service-native-multi:3.2.5-SNAPSHOT \
--amend goafabric/callee-service-native:3.2.5-SNAPSHOT --amend goafabric/callee-service-native-arm64v8:3.2.5-SNAPSHOT
docker manifest push goafabric/callee-service-native-multi:3.2.5-SNAPSHOT
docker run --pull always --name callee-service-native-multi --rm -p50900:50900 goafabric/callee-service-native-multi:3.2.5-SNAPSHOT -Xmx32m