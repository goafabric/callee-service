val nativeBuilder = "dashaun/builder:20230922"
tasks.register("dockerImageNative") { group = "build"; dependsOn("bootBuildImage") }
tasks.named<BootBuildImage>("bootBuildImage") {
    val nativeImageName = "${dockerRegistry}/${project.name}-native" + (if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else "") + ":${project.version}"
    builder.set(nativeBuilder)
    imageName.set(nativeImageName)
    environment.set(mapOf("BP_NATIVE_IMAGE" to "true", "BP_JVM_VERSION" to "21", "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "-J-Xmx5000m -march=compatibility"))
    doLast {
        exec { commandLine("docker", "run", "--rm", nativeImageName, "-check-integrity") }
        exec { commandLine("docker", "push", nativeImageName) }
    }
}