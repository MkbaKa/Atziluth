import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
    implementation(project(":hooker-mythicmobs-v472"))
    implementation(project(":hooker-mythicmobs-v411"))
    implementation(project(":hooker-mythicmobs-v544"))
}

tasks {
    withType<ShadowJar> {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")

        // taboolib
        relocate("taboolib", "me.mkbaka.atziluth.libs.taboolib")
        // reflections
        relocate("org.reflections", "me.mkbaka.atziluth.libs.reflections")

        destinationDirectory.set(File("E:\\Servers\\1.20.1\\plugins"))
    }
}