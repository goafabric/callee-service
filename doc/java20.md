# working
- spring boot 3.1
- jdk 20 + language level 20
- gradle 8.1 + build file with kotlin dsl

# also working
- jacoco with version 0.8.9
- github build
- kotlin 1.8

# not working
- native image build (local + container)
- graalvm 23 release pushed back to june

# graalvm23 ?

# features
## java 20
- Project Loom
- records (since java 17)
- java 21 LTs in september

## spring boot 3.1
- Dependency Upgrades
- Lots of Native Hints / Fixes (elasticsearch ...)
- Testcontainers support
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.1-Release-Notes

## quarkus 3.0
- Dependency Upgrades
- Upgrade to Hibernate 6 + Jakarta EE 10
- Support for Loom
- https://quarkus.io/blog/road-to-quarkus-3/


# script
- update gradle/gradle-wrapper.properties to 8.1
- show kotlin build file

- set intellij build jdk 20 (hint at new intellij version)
- set language level to 20 inside gradle
- fix / takeout jacoco

- upgrade jib baseimage : eclipse-temurin:20_36-jdk-ubi9-minimal

- update to spring boot 3.1.0-M2
- run
- build with jib and run

- features