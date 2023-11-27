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
        relocate("taboolib", "me.mkbaka.atziluth.taboolib")
        // kotlin
        relocate("kotlin.", "mkbaka.atziluth.libs.kotlin.") {
            exclude("kotlin.Metadata")
        }
        // javassist
        relocate("javassist", "mkbaka.atziluth.libs.javassist")
        // asm
        relocate("org.objectweb.asm", "mkbaka.atziluth.libs.asm7")
        // reflections
        relocate("org.reflections", "mkbaka.atziluth.libs.reflections")
        // nashorn
        // 用和 taboolib 相同的包名开头会被 AttributePlus 扫到
        // 然后在java8环境狠狠的抛UnsupportedClassVersionError
        relocate("org.openjdk.nashorn", "mkbaka.atziluth.libs.nashorn")
        // javax
        // relocate 会抛 java.lang.NoClassDefFoundError: mkbaka/atziluth/libs/javax/script/ScriptEngineFactory
//        relocate("javax", "mkbaka.atziluth.libs.javax")
        // slf4j
        relocate("org.slf4j", "mkbaka.atziluth.libs.slf4j")
        // intellij
        relocate("org.intellij", "mkbaka.atziluth.libs.intellij")
        // jetbrains
        relocate("org.jetbrains", "mkbaka.atziluth.libs.jetbrains")

        destinationDirectory.set(File("E:\\Servers\\1.20.1\\plugins"))
    }
}