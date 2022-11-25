# docker compose
go to /src/deploy/docker and do "./stack up" or "./stack up -native"

# run jvm multi image
docker run --pull always --name callee-service --rm -p50900:50900 goafabric/callee-service:3.0.1-kotlin-SNAPSHOT

# run native image
docker run --pull always --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:3.0.1-kotlin-SNAPSHOT -Xmx32m

# run native image arm
docker run --pull always --name callee-service-native-arm64v8 --rm -p50900:50900 goafabric/callee-service-native-arm64v8:3.0.1-kotlin-SNAPSHOT -Xmx32m
