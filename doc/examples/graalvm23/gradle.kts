tasks.register("dockerImageNative") { group = "build"; dependsOn("bootBuildImage") }
tasks.named<BootBuildImage>("bootBuildImage") {
    val nativeImageName = "${dockerRegistry}/${project.name}-native" + (if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else "") + ":${project.version}"
    if (System.getProperty("os.arch").equals("aarch64")) builder.set(nativeBuilder) else buildpacks.set(listOf("gcr.io/paketo-buildpacks/java-native-image:8.16.0"))
    environment.set(mapOf("BP_NATIVE_IMAGE" to "true", "BP_JVM_VERSION" to "17", "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "-J-Xmx4000m" + (if (!System.getProperty("os.arch").equals("aarch64")) " -march=compatibility" else "")))
    imageName.set(nativeImageName)
    doLast {
        exec { commandLine("docker", "run", "--rm", nativeImageName, "-check-integrity") }
        exec { commandLine("docker", "push", nativeImageName) }
    }
}