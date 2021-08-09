#docker compose
go to /src/deploy/docker and do "./stack up"

#run native image
docker pull goafabric/callee-service-native:1.1.0-SNAPSHOT && docker run --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:1.1.0-SNAPSHOT -Xmx64m
