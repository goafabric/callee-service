group = "org.goafabric"
version = "3.2.1-multi-module-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

val dockerRegistry = "goafabric"
val nativeBuilder = "dashaun/builder:20231204"
val baseImage = "ibm-semeru-runtimes:open-21.0.1_12-jre-focal@sha256:24d43669156684f7bc28536b22537a7533ab100bf0a5a89702b987ebb53215be"

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.2.0" apply(false) //apply false to beeing able to use dependency management, but not building an app
	id("io.spring.dependency-management") version "1.1.4"
}

//sub projects section so that subprojects can inherit dependency management and all other properties
subprojects {
	apply(plugin = "java") 
	apply(plugin = "io.spring.dependency-management")

	repositories {
		mavenCentral()
		maven { url = uri("https://repo.spring.io/milestone") }
		maven { url = uri("https://repo.spring.io/snapshot") }
	}

	dependencies {
		constraints {
			implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
			implementation("org.mapstruct:mapstruct:1.5.5.Final")
			annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
			implementation("io.github.resilience4j:resilience4j-spring-boot3:2.1.0")
		}
	}

	//https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#managing-dependencies.dependency-management-plugin.using-in-isolation
	dependencyManagement {
		imports {
			mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
		}
	}
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
    implementation("org.springframework.boot:spring-boot-starter-restclient")
}
//inside subprojects we can then do inside the dependencies section: implementation(project(":extensions"))

