
plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("jvm") version "1.7.10"
}

group = "io.github.highright1234"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

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
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}