import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
    implementation(project(":hooker-mythicmobs-v411"))
    implementation(project(":hooker-mythicmobs-v544"))
}

tasks {
    withType<ShadowJar> {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")

        // nashorn
        relocate("org.openjdk.nashorn", "me.mkbaka.libs.nashorn")
        // taboolib
        relocate("taboolib", "me.mkbaka.libs.taboolib")


        destinationDirectory.set(File("E:\\Servers\\1.20.1\\plugins"))
    }
}