import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

plugins {
    id("net.minecrell.plugin-yml.bukkit") version Versions.PLUGIN_YML
}

val monunLibraries = mutableListOf<String>()
fun DependencyHandlerScope.monunLibrary(name: String, version: String) {
    compileOnly("io.github.monun:$name-api:$version")
//    monunLibraries += "io.github.monun:$name-core:$version"
    library("io.github.monun:$name-core:$version")
}

repositories {
    maven("https://repo.rapture.pw/repository/maven-releases/")
    maven("https://repo.infernalsuite.com/repository/maven-snapshots/")
    mavenCentral()
}

dependencies {
    monunLibrary("tap", Versions.TAP)
    monunLibrary("invfx", Versions.INVFX)
    monunLibrary("kommand", Versions.KOMMAND)
    bukkitLibrary("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:${Versions.MC_COROUTINE}")
    bukkitLibrary("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:${Versions.MC_COROUTINE}")
    bukkitLibrary("io.github.highright1234:shotokonoko-bukkit:${Versions.SHOTOKONOKO}")
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}")
    library("io.lettuce:lettuce-core:6.2.0.RELEASE")
    library(kotlin("stdlib-jdk8"))
    library(kotlin("reflect"))

    // AdvancedSlimePaper
    compileOnly("com.infernalsuite.aswm:api:1.19.3-R0.1-SNAPSHOT")

//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
//    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
//    testImplementation("io.mockk:mockk:1.12.7")
//     https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
//    testImplementation("org.slf4j:slf4j-simple:2.0.0")
}

val directoryName = rootProject.name.replace("-", "")
val pluginName = rootProject.name.split("-").joinToString(separator = "") { it.capitalizeAsciiOnly() }

bukkit {
    name = pluginName
    main = "${rootProject.group}.$directoryName.$pluginName" + "Plugin"
    author = "HighRight"
    softDepend = listOf("SlimeWorldManager", "PlaceholderAPI")
    permissions {
        val op = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP
        register("minipaper.*") {
            children = listOf(
                "minipaper.command",
                "minipaper.bypass.*",
            )
        }
        register("minipaper.command") {
            default = op
        }
        register("minipaper.bypass.*") {
            childrenMap = mapOf(
                "minipaper.bypass.chat" to true,
                "minipaper.bypass.tab_list" to true,
            )
        }

        register("minipaper.bypass.chat") {
            default = op
        }
        register("minipaper.bypass.tab_list") {
            default = op
        }
    }
}