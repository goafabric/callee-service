FROM ibm-semeru-runtimes:open-21.0.4.1_7-jre-focal

# Set working directory
WORKDIR /app

# Copy the Spring Boot fat JAR
COPY build/libs/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-Xms128M", "-Xmx128M", "-jar", "app.jar"]

# bulding an image
# docker build -t goafabric/my-callee-service:1.0.0-SNAPSHOT -f src/deploy/Dockerfile .
# docker image ls | grep my-callee

# running a container
# docker run --rm -p50900:50900 goafabric/my-callee-service:1.0.0-SNAPSHOT
# docker container ls

# pushing and pulling to registry
# docker push goafabric/my-callee-service:1.0.0-SNAPSHOT
# docker pull goafabric/my-callee-service:1.0.0-SNAPSHOT
