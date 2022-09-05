
plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.dokka") version "1.7.10" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

extra.apply {
    set("pluginName", "MiniPaper")
    set("packageName", project.name)
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "org.jetbrains.dokka")

    if (project.name.contains("bungee")) {
        repositories {
            maven("https://oss.sonatype.org/content/repositories/snapshots")
        }
        dependencies {
            compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
        }
    } else {
        repositories {
            maven("https://repo.papermc.io/repository/maven-public/")
        }

        dependencies {
            compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
        }

    }

    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        compileOnly(kotlin("reflect"))
        compileOnly(kotlin("stdlib-jdk8"))
    }

    tasks {
        processResources {
            filesMatching("**/*.yml") {
                expand(rootProject.properties)
            }
        }

        if (project.name.endsWith("core")) {
            jar {
                dependsOn(shadowJar)
            }
        }
    }
    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
}