
val taboolib_version: String by rootProject

dependencies {
    implementation("org.openjdk.nashorn:nashorn-core:15.4")
    implementation("io.izzel.taboolib:common:$taboolib_version")
    implementation("io.izzel.taboolib:common-5:$taboolib_version")
    implementation("io.izzel.taboolib:module-chat:$taboolib_version")
    implementation("io.izzel.taboolib:module-configuration:$taboolib_version")
    implementation("io.izzel.taboolib:module-kether:$taboolib_version")
    implementation("io.izzel.taboolib:module-lang:$taboolib_version")
    implementation("io.izzel.taboolib:module-nms:$taboolib_version")
    implementation("io.izzel.taboolib:module-nms-util:$taboolib_version")
    implementation("io.izzel.taboolib:expansion-command-helper:$taboolib_version")
    implementation("io.izzel.taboolib:platform-bukkit:$taboolib_version")
    compileOnly(fileTree("libs"))
}

// 将plugin.yml中的"${version}"替换为插件版本
val replaceVersion = tasks.register("replaceVersionInPluginYml") {
    doLast {
        val inputFile = project.file("src/main/resources/plugin.yml")
        val outputFile = project.file("build/resources/main/plugin.yml")

        val inputText = inputFile.readText()

        val replacedText = inputText.replace("\${version}", version.toString())

        outputFile.writeText(replacedText)
    }
}

tasks.getByName("shadowJar").finalizedBy(replaceVersion)