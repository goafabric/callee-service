![Coverage](.github/badges/jacoco.svg)
![Branches](.github/badges/branches.svg)

# docker compose
go to /src/deploy/docker and do "./stack up" or "./stack up -native"

# run jvm multi image
docker run --pull always --name callee-service --rm -p50900:50900 goafabric/callee-service:3.2.5-SNAPSHOT

# run native image
docker run --pull always --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:3.2.5-SNAPSHOT -Xmx32m

# run native image arm
docker run --pull always --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native-arm64v8:3.2.5-SNAPSHOT -Xmx32m

# loki logger
docker run --pull always --name callee-service --rm -p50900:50900 --log-driver=loki --log-opt loki-url="http://host.docker.internal:3100/loki/api/v1/push" goafabric/callee-service:3.2.5-SNAPSHOT

# multiarch native
docker manifest create goafabric/callee-service-native-multi:3.2.5-SNAPSHOT \
--amend goafabric/callee-service-native:3.2.5-SNAPSHOT --amend goafabric/callee-service-native-arm64v8:3.2.5-SNAPSHOT 
docker manifest push goafabric/callee-service-native-multi:3.2.5-SNAPSHOT
docker run --pull always --name callee-service-native-multi --rm -p50900:50900 goafabric/callee-service-native-multi:3.2.5-SNAPSHOT -Xmx32m
