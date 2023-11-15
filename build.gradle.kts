import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val taboolib_version: String by project

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/public")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.codemc.io/repository/nms")
        mavenCentral()
        maven {
            url = uri("http://ptms.ink:8081/repository/releases/")
            isAllowInsecureProtocol = true
        }
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
        compileOnly("io.izzel.taboolib:common:$taboolib_version")
        compileOnly("io.izzel.taboolib:common-5:$taboolib_version")
        compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
        compileOnly("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
        compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=all", "-Xextended-compiler-checks")
        }
    }

    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/nexus/content/groups/public/")
    mavenCentral()
    maven {
        url = uri("http://ptms.ink:8081/repository/releases/")
        isAllowInsecureProtocol = true
    }
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        exclude("META-INF/maven/**")
        exclude("META-INF/tf/**")
        exclude("module-info.java")

        kotlinSourcesJar {
            rootProject.subprojects.forEach { from(it.sourceSets["main"].allSource) }
        }

        build {
            dependsOn(shadowJar)
        }
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}