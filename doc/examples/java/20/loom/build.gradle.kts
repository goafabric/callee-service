java.sourceCompatibility = JavaVersion.VERSION_20
tasks.withType<JavaCompile> { options.compilerArgs.add("--enable-preview") }
tasks.withType<JavaExec> { jvmArgs("--enable-preview") }
tasks.withType<Test> { jvmArgs("--enable-preview") }