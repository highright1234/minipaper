import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

plugins {
    kotlin("jvm") version Versions.KOTLIN
    id("io.gitlab.arturbosch.detekt") version Versions.DETEKT
}

group = "io.github.highright1234"
version = "0.0.1"

fun Project.subproject(tag : String) =
    name
        .split("-")
        .slice(0..1)
        .joinToString(separator = "-")
        .plus("-$tag")
        .let { ":$it" }
        .let(rootProject::project)

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.papermc.io/repository/maven-public/")
        mavenCentral()
    }
    dependencies {
        if ("bukkit" in project.name) {
            compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
        }
        if ("bungee" in project.name) {
            compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
        }
        if ("core" in project.name ) {
            implementation(subproject("api"))
        }
        if ("debug" in project.name) {
            compileOnly(subproject("api"))
        }
        if ("common" !in project.name) {
            implementation(project(":${rootProject.name}-common"))
        }
        compileOnly(kotlin("stdlib-jdk8"))
        compileOnly(kotlin("reflect"))
    }
    if ("core" in project.name || "debug" in project.name) {
        tasks.register<Jar>("pluginsUpdate") {
            var pluginName = rootProject.name.split("-").joinToString(separator = "") { it.capitalizeAsciiOnly() }
            if ("debug" in project.name) pluginName += "Debug"
            archiveBaseName.set(pluginName)
            from(sourceSets["main"].output)
            from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
            val serverDir = File(rootProject.rootDir, ".server")
            doLast {
                // 내 마크 서버 환경 불러오기
                val serverFolder = File("E:\\.bungee\\")
                if (!serverDir.exists() && serverFolder.exists()) {
                    serverDir.mkdir()
                    copy {
                        from(serverFolder)
                        include("**/**")
                        into(serverDir)
                    }
                }
                val bukkits = serverDir
                    .listFiles()!!
                    .filter { "server" in it.name }
                val proxy = File(serverDir, "proxy")

                val pluginsFolders: List<File> =
                    if ("bukkit" in project.name) {
                        bukkits.map { File(it, "plugins") }
                    } else if ("bungee" in project.name) {
                        File(proxy, "plugins").let(::listOf)
                    } else {
                        return@doLast
                    }

                pluginsFolders.forEach {
                    copy {
                        from(archiveFile)
                        if (File(it, archiveFileName.get()).exists()) {
                            File(it, archiveFileName.get()).delete()
                        }
                        into(it)
                    }
                }
                pluginsFolders.forEach {
                    // auto-reloader
                    val updateFolder = File(it, "update")
                    if (!updateFolder.exists()) return@doLast
                    File(updateFolder, "RELOAD").delete()
                }
            }
        }
        tasks.named("build") { finalizedBy("pluginsUpdate") }
    }

    java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
