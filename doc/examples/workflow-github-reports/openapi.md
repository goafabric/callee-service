id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
openApi {  apiDocsUrl.set("http://localhost:50800/v3/api-docs") }
./gradlew clean generateOpenApiDocs