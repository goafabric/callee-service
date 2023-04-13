import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

group = "org.goafabric"
version = "3.0.5-kts-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.graalvm.buildtools.native") version "0.9.20"
	id("com.google.cloud.tools.jib") version "3.3.1"
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	constraints {
		implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
		implementation("org.mapstruct:mapstruct:1.5.3.Final")
		annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
		implementation("io.github.resilience4j:resilience4j-spring-boot3:2.0.2")
	}
}

dependencies {
	//web
	implementation("org.springframework.boot:spring-boot-starter-web")

	//monitoring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")

	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	implementation("io.zipkin.reporter2:zipkin-reporter-brave")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")

	//crosscuting
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-aop")

	//test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("io.github.resilience4j:resilience4j-spring-boot3")

}

tasks.withType<Test> {
	useJUnitPlatform()
	exclude("**/*NRIT*")
	finalizedBy("jacocoTestReport")
}

val dockerRegistry = "goafabric"
val baseImage = "ibm-semeru-runtimes:open-17.0.6_10-jre-focal@sha256:739eab970ff538cf22a20b768d7755dad80922a89b73b2fddd80dd79f9b880a1";
val nativeBuilderImage = "dashaun/builder:20230225"
val archSuffix = ""

jib {
	from.image = baseImage
	to.image = "$dockerRegistry/callee-service:3.0.5-kts-SNAPSHOT"
	//to.image = "$dockerRegistry/$project.name:$project.version"
	container.jvmFlags = listOf("-Xms256m", "-Xmx256m")
	//from.platforms = [com.google.cloud.tools.jib.gradle.PlatformParameters.of("linux/amd64"), com.google.cloud.tools.jib.gradle.PlatformParameters.of("linux/arm64")]
}

tasks.named<BootBuildImage>("bootBuildImage") {
	builder.set(nativeBuilderImage)
	imageName.set("${dockerRegistry}/${project.name}-native${archSuffix}:${project.version}")
	environment.set(mapOf("BP_NATIVE_IMAGE" to "true", "BP_JVM_VERSION" to "17", "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "-J-Xmx4000m"))
}

task<Exec>("dockerImageNativeRun") {
	//dependsOn("bootBuildImage")
	commandLine("echo", "test")
	//commandLine 'docker', 'run', "--rm", "${dockerRegistry}/${project.name}-native${archSuffix}:${project.version}", '-check-integrity'
}

task<Exec>("dockerImageNative") {
	dependsOn("dockerImageNativeRun")
	commandLine("echo", "test")
	//commandLine 'docker', 'push', "${dockerRegistry}/${project.name}-native${archSuffix}:${project.version}"
}
