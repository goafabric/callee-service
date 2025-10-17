group = "org.goafabric"
version = "3.0.5-spark-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
	java
	jacoco
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.apache.spark:spark-core_2.13:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
    implementation("org.springframework.boot:spring-boot-starter-restclient") //{exclude("org.apache.logging.log4j", "log4j-slf4j2-impl")}
}

tasks.withType<JavaExec> { jvmArgs("--add-opens=java.base/sun.nio.ch=ALL-UNNAMED") }

tasks.withType<Test> {
	useJUnitPlatform()
	exclude("**/*NRIT*")
	finalizedBy("jacocoTestReport")
}

