FROM ibm-semeru-runtimes:open-21.0.4.1_7-jre-focal

# Set working directory
WORKDIR /app

# Copy dependency libraries, resources, and compiled classes from Gradle build output
COPY build/libs /app/libs
COPY build/classes/java/main /app/classes
COPY build/resources/main /app/resources

# Define the entrypoint
ENTRYPOINT ["java", "-Xms128M", "-Xmx128M", "-cp", "/app/resources:/app/classes:/app/libs/*", "org.goafabric.calleeservice.Application"]


# docker build -t goafabric/my-callee-service:1.0.0-SNAPSHOT -f src/deploy/Dockerfile .
# docker run --rm goafabric/my-callee-service:1.0.0-SNAPSHOT
