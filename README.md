#docker compose
go to /src/deploy/docker and do "./stack up"

#run jvm multi image
docker pull goafabric/callee-service:3.0.5-console-SNAPSHOT && docker run --name callee-service --rm -p50900:50900 goafabric/callee-service:3.0.5-console-SNAPSHOT

#run native image
docker pull goafabric/callee-service-native:3.0.5-console-SNAPSHOT && docker run --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:3.0.5-console-SNAPSHOT -Xmx32m

#run native image arm
docker pull goafabric/callee-service-native-arm64v8:3.0.5-console-SNAPSHOT && docker run --name callee-service-native-arm64v8 --rm -p50900:50900 goafabric/callee-service-native-arm64v8:3.0.5-console-SNAPSHOT -Xmx32m
