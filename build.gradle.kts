import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

val group: String by project
val version: String by project
java.sourceCompatibility = JavaVersion.VERSION_21

val dockerRegistry = "goafabric"
val nativeBuilder = "paketobuildpacks/java-native-image:9.5.0"
val baseImage = "ibm-semeru-runtimes:open-21.0.3_9-jre-focal@sha256:5cb19afa9ee0daeecb7c31be8253fecbbf6b5f6dcfb06883c41f045cb893bcec"

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.graalvm.buildtools.native") version "0.10.2"

	id("com.google.cloud.tools.jib") version "3.4.2"
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
	implementation("org.springframework.boot:spring-boot-starter-aop")

	implementation("com.nimbusds:nimbus-jose-jwt:9.22")
	
	//implementation("com.github.ben-manes.caffeine:caffeine"); implementation("org.springframework.boot:spring-boot-starter-cache");
	//implementation("org.springframework.boot:spring-boot-starter-data-redis"); implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml"); implementation("org.springframework.boot:spring-boot-starter-cache");

	//s3
	//implementation("io.awspring.cloud:spring-cloud-aws-starter-s3:3.1.0")
	implementation("org.springframework.boot:spring-boot-starter-web"); implementation("am.ik.s3:simple-s3-client:0.2.1") {exclude("org.springframework", "spring-web")}; implementation("org.springframework.boot:spring-boot-starter-web");
	implementation("com.azure:azure-storage-blob:12.28.1")

	//implementation("com.itextpdf:itext-core:8.0.2")

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

tasks.register("dockerImageNative") { description= "Native Image"; group = "build"; dependsOn("bootBuildImage") }
tasks.named<BootBuildImage>("bootBuildImage") {
	val nativeImageName = "${dockerRegistry}/${project.name}-native" + (if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else "") + ":${project.version}"
	builder.set("paketobuildpacks/builder-jammy-buildpackless-tiny")
	buildpacks.add(nativeBuilder)
	imageName.set(nativeImageName)
	environment.set(mapOf("BP_NATIVE_IMAGE" to "true", "BP_JVM_VERSION" to "21", "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "-J-Xmx5000m -march=compatibility --initialize-at-build-time=org.slf4j.helpers.Reporter"))
	doLast {
		exec { commandLine("/bin/sh", "-c", "docker run --rm $nativeImageName -check-integrity") }
		exec { commandLine("/bin/sh", "-c", "docker push $nativeImageName") }
	}
}

graalvmNative {
	binaries.named("main") {
		buildArgs.add("--initialize-at-build-time=org.slf4j.helpers.Reporter") //required for azure blob from boot 3.3.3+
	}
}