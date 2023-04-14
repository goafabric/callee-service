import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.goafabric"
version = "3.0.5-kotlinSNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
	jacoco
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.graalvm.buildtools.native") version "0.9.20"
	id("com.google.cloud.tools.jib") version "3.3.1"

	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
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

	//kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks.withType<Test> {
	useJUnitPlatform()
	exclude("**/*NRIT*")
	finalizedBy("jacocoTestReport")
}

val dockerRegistry = "goafabric"
val nativeBuilderImage = "dashaun/builder:20230225"
val baseImage = "ibm-semeru-runtimes:open-17.0.6_10-jre-focal@sha256:739eab970ff538cf22a20b768d7755dad80922a89b73b2fddd80dd79f9b880a1"
val archSuffix = if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else ""

jib {
	val amd64 = com.google.cloud.tools.jib.gradle.PlatformParameters(); amd64.os = "linux"; amd64.architecture = "amd64"
	val arm64 = com.google.cloud.tools.jib.gradle.PlatformParameters(); arm64.os = "linux"; arm64.architecture = "arm64"
	from.image = baseImage
	to.image = "${dockerRegistry}/${project.name}:${project.version}"
	container.jvmFlags = listOf("-Xms256m", "-Xmx256m")
	from.platforms.set(listOf(amd64, arm64))
}

tasks.named<BootBuildImage>("bootBuildImage") {
	builder.set(nativeBuilderImage)
	imageName.set("${dockerRegistry}/${project.name}-native${archSuffix}:${project.version}")
	environment.set(mapOf("BP_NATIVE_IMAGE" to "true", "BP_JVM_VERSION" to "17", "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "-J-Xmx4000m"))
}

task<Exec>("dockerImageNativeRun") { dependsOn("bootBuildImage")
	commandLine ("docker", "run", "--rm", "${dockerRegistry}/${project.name}-native${archSuffix}:${project.version}", "-check-integrity")
}

task<Exec>("dockerImageNative") { dependsOn("dockerImageNativeRun")
	commandLine("docker", "push", "${dockerRegistry}/${project.name}-native${archSuffix}:${project.version}")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}
