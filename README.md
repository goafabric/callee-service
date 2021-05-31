#run native image
docker pull goafabric/callee-service-native:1.0.4-SNAPSHOT && docker run --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:1.0.4-SNAPSHOT -Xmx64m
