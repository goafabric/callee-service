buildscript { dependencies { classpath("com.google.cloud.tools:jib-native-image-extension-gradle:0.1.0") }}
tasks.register("dockerImageNativeNoTest") {group = "build"; dependsOn("bootJar")
	jib.to.image = ""
	doFirst {
		exec { commandLine(
			"docker", "run", "--rm", "--mount", "type=bind,source=${projectDir}/build,target=/build", "--entrypoint", "/bin/bash", graalvmBuilderImage, "-c", """ mkdir -p /build/native/nativeCompile && cp /build/libs/*.jar /build/native/nativeCompile && cd /build/native/nativeCompile && jar -xvf *.jar &&
			native-image -J-Xmx5000m -march=compatibility -H:Name=application $([[ -f META-INF/native-image/argfile ]] && echo @META-INF/native-image/argfile) -cp .:BOOT-INF/classes:$(ls -d -1 "/build/native/nativeCompile/BOOT-INF/lib/"*.* | tr "\n" ":") && /build/native/nativeCompile/application -check-integrity """
		)}
		jib.from.image = "ubuntu:22.04"
		jib.to.image = "${dockerRegistry}/${project.name}-native" + (if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else "") + ":${project.version}"
		jib.pluginExtensions { pluginExtension {properties = mapOf("imageName" to "application"); implementation = "com.google.cloud.tools.jib.gradle.extension.nativeimage.JibNativeImageExtension" }}
	}
	finalizedBy("jib")
}
tasks.register("dockerImageNative") {group = "build"; dependsOn("clean", "dockerImageNativeNoTest"); doLast { exec { commandLine("docker", "run", "--rm", "--pull", "always", "${dockerRegistry}/${project.name}-native" + (if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else "") + ":${project.version}", "-check-integrity") } } }
