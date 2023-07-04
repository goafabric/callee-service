![Coverage](.github/badges/jacoco.svg)
![Branches](.github/badges/branches.svg)

# docker compose
go to /src/deploy/docker and do "./stack up" or "./stack up -native"

# run jvm multi image
docker run --pull always --name callee-service --rm -p50900:50900 goafabric/callee-service:3.1.2-SNAPSHOT

# run native image
docker run --pull always --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:3.1.2-SNAPSHOT -Xmx32m

# run native image arm
docker run --pull always --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native-arm64v8:3.1.2-SNAPSHOT -Xmx32m

# loki logger
docker run --pull always --name callee-service --rm -p50900:50900 --log-driver=loki --log-opt loki-url="http://host.docker.internal:3100/loki/api/v1/push" goafabric/callee-service:3.1.2-SNAPSHOT
