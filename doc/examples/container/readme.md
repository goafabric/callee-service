# doc
https://github.com/apple/container
https://github.com/apple/container/releases

# system
container system start
container system stop

# run
container ls (identify ip address)
container image ls
             
# images

## postgres
container run --rm --name postgres -e POSTGRES_PASSWORD=postgres postgres:17.4

# run native image arm
container run --rm goafabric/callee-service-native:3.5.1-SNAPSHOT



# images not working

## run jvm multi image
container run --rm goafabric/callee-service:3.5.1-SNAPSHOT

## run native image x86 
container run --rm goafabric/callee-service-native:3.4.1-SNAPSHOT

