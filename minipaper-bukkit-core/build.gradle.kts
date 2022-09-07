plugins {
    `maven-publish`
}

val api = project(":${rootProject.name}-bukkit-api")

dependencies {
    compileOnly("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.4.0")
    compileOnly("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.4.0")
    compileOnly("io.github.monun:kommand-api:${properties["kommandVersion"]}")
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