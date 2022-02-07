#docker compose
go to /src/deploy/docker and do "./stack up"

#run jvm multi image
docker pull goafabric/callee-service:1.2.1-SNAPSHOT && docker run --name callee-service --rm -p50900:50900 goafabric/callee-service:1.2.1-SNAPSHOT

#run native image
docker pull goafabric/callee-service-native:1.2.1-SNAPSHOT && docker run --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:1.2.1-SNAPSHOT -Xmx64m
                
#force amd64
docker pull goafabric/callee-service:1.2.1-SNAPSHOT && docker run --platform linux/amd64 --name callee-service --rm -p50900:50900 goafabric/callee-service:1.2.1-SNAPSHOT
