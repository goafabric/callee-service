#docker compose
go to /src/deploy/docker and do "./stack up"

#run arm64 image
docker pull goafabric/callee-service-arm64v8:1.1.1-SNAPSHOT && docker run --name callee-service-native --rm -p50900:50900 goafabric/callee-service-arm64v8:1.1.1-SNAPSHOT -Xmx128m

#run native image
docker pull goafabric/callee-service-native:1.1.1-SNAPSHOT && docker run --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:1.1.1-SNAPSHOT -Xmx64m
