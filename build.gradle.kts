group = "org.goafabric"
version = "3.1.2-console-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val dockerRegistry = "goafabric"
val graalvmBuilderImage = "ghcr.io/graalvm/native-image-community:20.0.2" //"ghcr.io/graalvm/native-image-community:17.0.8"
val baseImage = "ibm-semeru-runtimes:open-20.0.1_9-jre-focal@sha256:f1a10da50d02f51e79e3c9604ed078a39c19cd2711789cab7aa5d11071482a7e"
jacoco.toolVersion = "0.8.9"

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.graalvm.buildtools.native") version "0.9.23"
	id("com.google.cloud.tools.jib") version "3.3.2"
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	constraints {
		implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
		implementation("org.mapstruct:mapstruct:1.5.5.Final")
		annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
		implementation("io.github.resilience4j:resilience4j-spring-boot3:2.1.0")
	}
}

dependencies {
	//web
	implementation("org.springframework.boot:spring-boot-starter")

	//crosscuting
	implementation("org.springframework.boot:spring-boot-starter-aop")

	//implementation("org.springframework.boot:spring-boot-starter-cache")
	//implementation("com.github.ben-manes.caffeine:caffeine")

	//s3
	//implementation("io.awspring.cloud:spring-cloud-aws-starter-s3:3.0.1")


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

buildscript { dependencies { classpath("com.google.cloud.tools:jib-native-image-extension-gradle:0.1.0") }}
tasks.register("dockerImageNativeNoTest") {group = "build"; dependsOn("bootJar")
	exec { commandLine(
		"docker", "run", "--rm", "--mount", "type=bind,source=${projectDir}/build,target=/build", "--entrypoint", "/bin/bash", graalvmBuilderImage, "-c", """ mkdir -p /build/native/nativeCompile && cp /build/libs/*-SNAPSHOT.jar /build/native/nativeCompile && cd /build/native/nativeCompile && jar -xvf *.jar &&
		native-image -J-Xmx5000m -march=compatibility -H:Name=application $([[ -f META-INF/native-image/argfile ]] && echo @META-INF/native-image/argfile) -cp .:BOOT-INF/classes:$(ls -d -1 "/build/native/nativeCompile/BOOT-INF/lib/"*.* | tr "\n" ":") && /build/native/nativeCompile/application -check-integrity """
	)}
	jib.from.image = "ubuntu:22.04"
	jib.to.image = "${dockerRegistry}/${project.name}-native" + (if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else "") + ":${project.version}"
	jib.pluginExtensions { pluginExtension {properties = mapOf("imageName" to "application"); implementation = "com.google.cloud.tools.jib.gradle.extension.nativeimage.JibNativeImageExtension" }}
	finalizedBy("jib")
}
tasks.register("dockerImageNative") {group = "build"; dependsOn("dockerImageNativeNoTest"); doLast { exec { commandLine("docker", "run", "--rm", "--pull", "always", "${dockerRegistry}/${project.name}-native" + (if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else "") + ":${project.version}", "-check-integrity") } } }

graalvmNative { //https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html#configuration-options
	binaries.named("main") { quickBuild.set(true) }
}