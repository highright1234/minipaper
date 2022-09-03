plugins {
    id("org.jetbrains.dokka") version "1.7.10"
    `maven-publish`
}

val api = project(":${rootProject.name}-api")

dependencies {
    compileOnly("io.github.monun:kommand-api:2.13.0")
    implementation(api)
}

tasks {
    jar {
        archiveClassifier.set("core")
    }

    register<Jar>("paperJar") {
        from(sourceSets["main"].output)

        subprojects.forEach {
            val paperJar = it.tasks.jar.get()
            dependsOn(paperJar)
            from(zipTree(paperJar.archiveFile))
        }
    }

    register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        (listOf(project) + subprojects).forEach { from(it.sourceSets["main"].allSource) }
    }

    register<Jar>("dokkaJar") {
        archiveClassifier.set("javadoc")
        dependsOn("dokkaHtml")

        from("$buildDir/dokka/html/") {
            include("**")
        }
    }
}