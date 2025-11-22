import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

val version: String by project
val javaVersion = "24"
java.sourceCompatibility = JavaVersion.toVersion(javaVersion)
tasks.withType<KotlinCompile>().all { compilerOptions { jvmTarget.set(JvmTarget.fromTarget(javaVersion)) } }

val dockerRegistry = "goafabric"
val baseImage = "eclipse-temurin:25-jre@sha256:74d5c631e5db5a44e7f5a2dd49f93f0c6f7b8c22c1dc1b8e1caec7009872c5c3"

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.5.8"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.graalvm.buildtools.native") version "0.11.3"

	id("com.google.cloud.tools.jib") version "3.5.1"
	id("net.researchgate.release") version "3.1.0"
	id("org.sonarqube") version "7.1.0.6387"

	id("org.cyclonedx.bom") version "3.0.2"
	id("org.springdoc.openapi-gradle-plugin") version "1.9.0"

	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	constraints {
		annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
		implementation("org.mapstruct:mapstruct:1.6.3")
		implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0")
		implementation("io.github.resilience4j:resilience4j-spring-boot3:2.3.0")
		implementation("net.ttddyy.observation:datasource-micrometer-spring-boot:1.2.0")
		implementation("org.mockito.kotlin:mockito-kotlin:6.1.0")
		testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")
	}
}

dependencies {
	//web
	implementation("org.springframework.boot:spring-boot-starter-web")
	
	//monitoring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")

	implementation("io.micrometer:micrometer-tracing-bridge-otel")
	implementation("io.opentelemetry:opentelemetry-exporter-otlp")

	//openapi
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")

	//crosscuting
	implementation("org.springframework.boot:spring-boot-starter-aop")

	//kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	testImplementation("com.tngtech.archunit:archunit-junit5")

	//test
	testImplementation("org.mockito.kotlin:mockito-kotlin")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.github.resilience4j:resilience4j-spring-boot3")
}

tasks.withType<Test> {
	useJUnitPlatform()
	exclude("**/*NRIT*")
	finalizedBy("jacocoTestReport")
}
tasks.jacocoTestReport { reports {csv.required.set(true); xml.required.set(true) } }

jib {
	val amd64 = com.google.cloud.tools.jib.gradle.PlatformParameters(); amd64.os = "linux"; amd64.architecture = "amd64"; val arm64 = com.google.cloud.tools.jib.gradle.PlatformParameters(); arm64.os = "linux"; arm64.architecture = "arm64"
	from.image = baseImage
	to.image = "${dockerRegistry}/${project.name}:${project.version}"
	container.jvmFlags = listOf("-Xms256m", "-Xmx256m")
	from.platforms.set(listOf(amd64, arm64))
}

interface InjectedExecOps { @get:Inject val execOps: ExecOperations }
tasks.register("dockerImageNative") { description= "Native Image"; group = "build"; dependsOn("bootBuildImage") }
tasks.named<BootBuildImage>("bootBuildImage") {
	val nativeImageName = "${dockerRegistry}/${project.name}-native:${project.version}"
	imageName.set(nativeImageName)
	environment.set(mapOf("BP_NATIVE_IMAGE" to "true", "BP_JVM_VERSION" to "25", "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "-J-Xmx5000m -march=compatibility"))
	doLast {
		project.objects.newInstance<InjectedExecOps>().execOps.exec { commandLine("/bin/sh", "-c", "docker run --rm $nativeImageName -check-integrity") }
		project.objects.newInstance<InjectedExecOps>().execOps.exec { commandLine("/bin/sh", "-c", "docker push $nativeImageName") }
	}
}

configure<net.researchgate.release.ReleaseExtension> {
	buildTasks.set(listOf("build", "test", "jib", "dockerImageNative"))
	tagTemplate.set("v${version}".replace("-SNAPSHOT", ""))
}

//tasks.cyclonedxBom { destination = file("doc/generated") }
openApi {
	outputDir.set(file("doc/generated"))
	customBootRun { args.set(listOf("--server.port=8080")) }
	tasks.forkedSpringBootRun { dependsOn("compileAotJava", "processAotResources") }
}

kotlin.compilerOptions.freeCompilerArgs.add("-Xannotation-default-target=param-property")
