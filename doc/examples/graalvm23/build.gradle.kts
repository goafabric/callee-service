import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

group = "org.goafabric"
version = "3.2.3-console-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val dockerRegistry = "goafabric"
val nativeBuilder = "dashaun/builder:20230225"
val baseImage = "ibm-semeru-runtimes:open-20.0.1_9-jre-focal@sha256:f1a10da50d02f51e79e3c9604ed078a39c19cd2711789cab7aa5d11071482a7e"
jacoco.toolVersion = "0.8.9"

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.graalvm.buildtools.native") version "0.9.23"
	id("com.google.cloud.tools.jib") version "3.3.1"
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	constraints {
		implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
		implementation("org.mapstruct:mapstruct:1.5.4.Final")
		annotationProcessor("org.mapstruct:mapstruct-processor:1.5.4.Final")
		implementation("io.github.resilience4j:resilience4j-spring-boot3:2.0.2")
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

tasks.register("dockerImageNative") { group = "build"; dependsOn("bootBuildImage") }
tasks.named<BootBuildImage>("bootBuildImage") {
	val nativeImageName = "${dockerRegistry}/${project.name}-native" + (if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else "") + ":${project.version}"
	builder.set(nativeBuilder)
	//builder.set("paketobuildpacks/builder:tiny")
	imageName.set(nativeImageName)
	environment.set(mapOf("BP_NATIVE_IMAGE" to "true", "BP_JVM_VERSION" to "17", "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "-J-Xmx4000m"))
	doLast {
		exec { commandLine("docker", "run", "--rm", nativeImageName, "-check-integrity") }
		exec { commandLine("docker", "push", nativeImageName) }
	}
}

val graalvmImage = "ghcr.io/graalvm/native-image-community:17.0.8"
tasks.register("buildNativeImage") {group = "build"; dependsOn("bootJar")
	val graalvmOptions = "-J-Xmx5000m -Ob -march=compatibility -H:Name=application"
	doLast {
		exec {
			commandLine(
				"docker", "run", "--rm", "--name", "native-builder", "--mount", "type=bind,source=${projectDir}/build,target=/build", "--entrypoint", "/bin/bash", graalvmImage, "-c", """
				mkdir -p /build/native/nativeCompile && cp /build/libs/*-SNAPSHOT.jar /build/native/nativeCompile && cd /build/native/nativeCompile && jar -xvf *.jar &&
				native-image $graalvmOptions $([[ -f META-INF/native-image/argfile ]] && echo @META-INF/native-image/argfile) -cp .:BOOT-INF/classes:$(ls -d -1 "/build/native/nativeCompile/BOOT-INF/lib/"*.* | tr "\n" ":") """
			)
		}
		//exec {commandLine("./container-compile.sh") }
	}
}

buildscript { dependencies { classpath("com.google.cloud.tools:jib-native-image-extension-gradle:0.1.0") }}
tasks.register("jibNativeImage") {group = "build"; //dependsOn("buildNativeImage")
	val nativeImageName = "${dockerRegistry}/${project.name}-native" + (if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else "") + ":${project.version}"
	doFirst {
		jib.from.image = "ubuntu:22.04" //"ghcr.io/graalvm/native-image-community:17.0.8"
		jib.to.image = nativeImageName
		jib.pluginExtensions {
			pluginExtension {
				implementation = "com.google.cloud.tools.jib.gradle.extension.nativeimage.JibNativeImageExtension"
				properties = mapOf("imageName" to "application")
			}
		}
		/*
		jib.extraDirectories {
			paths {
				path {
					setFrom("build/native/nativeCompile")
					into = "/app"
					permissions.set(mutableMapOf("/app/application" to "755"))
					includes.set(mutableListOf("application"))
				}
			}
			
		}
		jib.container.entrypoint = mutableListOf("/app/application")

		 */
	}
	doLast {
		exec { commandLine("docker", "run", "--rm", "--pull", "always" ,nativeImageName, "-check-integrity") }
	}
	finalizedBy("jib")
}

graalvmNative { //https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html#configuration-options
	binaries.named("main") { quickBuild.set(true) }
}