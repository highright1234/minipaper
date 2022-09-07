
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
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
        testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
        testImplementation("io.mockk:mockk:1.12.1")
        // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
        testImplementation("org.slf4j:slf4j-simple:2.0.0")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
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