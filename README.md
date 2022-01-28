#docker compose
go to /src/deploy/docker and do "./stack up"

#run arm64 image
docker pull goafabric/callee-service-arm64v8:1.2.1-multi-SNAPSHOT && docker run --name callee-service-native --rm -p50900:50900 goafabric/callee-service-arm64v8:1.2.1-SNAPSHOT -Xmx128m

#run native image
docker pull goafabric/callee-service-native:1.2.1-SNAPSHOT && docker run --name callee-service-native --rm -p50900:50900 goafabric/callee-service-native:1.2.1-SNAPSHOT -Xmx64m
          
#multi
docker run --rm --name callee-service goafabric/callee-service:1.2.1-multi-SNAPSHOT

docker run --platform linux/amd64 --rm --name callee-service goafabric/callee-service:1.2.1-multi-SNAPSHOT
                                                        
docker manifest inspect goafabric/callee-service:1.2.1-multi-SNAPSHOT

#postgres
docker run --rm --name postgres -e POSTGRES_PASSWORD=password postgres:14.1

docker run --platform linux/amd64 --rm --name postgres -e POSTGRES_PASSWORD=password postgres:14.1