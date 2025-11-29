import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

val version: String by project
val javaVersion = "25"
java.sourceCompatibility = JavaVersion.toVersion(javaVersion)

val dockerRegistry = "goafabric"
val baseImage = "ibm-semeru-runtimes:open-jdk-25.0.0_36-jre@sha256:8ae073345116cfd51ec37b26c3a1c25de9336d436354e0be4271bda1463e119c"

plugins {
	java
	jacoco
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.graalvm.buildtools.native") version "0.11.3"

	id("com.google.cloud.tools.jib") version "3.5.1"
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	constraints {
		annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
		implementation("org.mapstruct:mapstruct:1.5.5.Final")
		implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
		implementation("org.mapstruct:mapstruct:1.5.5.Final")
	}
}

dependencies {
	//web
	implementation("org.springframework.boot:spring-boot-starter")

	//crosscuting
	implementation("org.springframework.boot:spring-boot-starter-aspectj")

	//implementation("com.nimbusds:nimbus-jose-jwt:9.22")

	//implementation("com.github.ben-manes.caffeine:caffeine"); implementation("org.springframework.boot:spring-boot-starter-cache");
	//implementation("org.springframework.boot:spring-boot-starter-data-redis"); implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml"); implementation("org.springframework.boot:spring-boot-starter-cache");

	//s3
	//implementation("io.awspring.cloud:spring-cloud-aws-starter-s3:3.4.0");
	//implementation("com.azure:azure-storage-blob:12.31.3"); implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")

	//implementation("org.springframework.boot:spring-boot-starter-web"); implementation("am.ik.s3:simple-s3-client:0.2.1") {exclude("org.springframework", "spring-web")}; implementation("org.springframework.boot:spring-boot-starter-web");

	//pdf
	//implementation("com.github.librepdf:openpdf:2.0.3")

	//statemachine
	//implementation("org.springframework.statemachine:spring-statemachine-starter:4.0.0")

	//drools
	//implementation("org.drools:drools-core:10.0.0"); implementation("org.drools:drools-compiler:10.0.0"); implementation("org.drools:drools-mvel:10.0.0")

	//fabric8
	//implementation("io.fabric8:kubernetes-client:7.3.1")

	//test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
	exclude("**/*NRIT*")
	finalizedBy("jacocoTestReport")
}
tasks.jacocoTestReport { reports {csv.required.set(true) } }

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
	val nativeImageName = "${dockerRegistry}/${project.name}-native" + (if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else "") + ":${project.version}"
	imageName.set(nativeImageName)
	environment.set(mapOf("BP_NATIVE_IMAGE" to "true", "BP_JVM_VERSION" to javaVersion, "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "-J-Xmx5000m -march=compatibility --initialize-at-build-time=org.slf4j,ch.qos.logback,com.fasterxml.jackson.core")) //azure blob
	doLast {
		project.objects.newInstance<InjectedExecOps>().execOps.exec { commandLine("/bin/sh", "-c", "docker run --rm $nativeImageName -check-integrity") }
		project.objects.newInstance<InjectedExecOps>().execOps.exec { commandLine("/bin/sh", "-c", "docker push $nativeImageName") }
	}
}

graalvmNative {
	binaries.named("main") {
        buildArgs.add("--initialize-at-build-time=org.slf4j,ch.qos.logback,com.fasterxml.jackson.core") //azure blob
	}
}
