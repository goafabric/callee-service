# docker compose
go to /src/deploy/docker and do "./stack up" or "./stack up -native"

# run jvm multi image
docker run --pull always --name callee-service --rm -p 50900:50900 goafabric/callee-service:$(grep '^version=' gradle.properties | cut -d'=' -f2)

# run native image
docker run --pull always --name callee-service-native --rm -p 50900:50900 goafabric/callee-service-native:$(grep '^version=' gradle.properties | cut -d'=' -f2) -Xmx32m
             
# container
"${(@z)${CRUNTIME:-docker run --pull always}}"  --name callee-service --rm -p 50900:50900 goafabric/callee-service:$(grep '^version=' gradle.properties | cut -d'=' -f2)

"${(@z)${CRUNTIME:-docker run --pull always}}" --name callee-service-native --rm -p 50900:50900 goafabric/callee-service-native:$(grep '^version=' gradle.properties | cut -d'=' -f2) -Xmx32m

