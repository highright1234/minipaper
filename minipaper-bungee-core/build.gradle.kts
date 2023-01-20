import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

plugins {
    id("net.minecrell.plugin-yml.bungee") version Versions.PLUGIN_YML
}

val directoryName = rootProject.name.replace("-", "")
val pluginName = rootProject.name.split("-").joinToString(separator = "") { it.capitalizeAsciiOnly() }
val thisPluginName = pluginName

repositories {
    mavenCentral()
}

dependencies {
    bungeeLibrary("com.github.shynixn.mccoroutine:mccoroutine-bungeecord-api:${Versions.MC_COROUTINE}")
    bungeeLibrary("com.github.shynixn.mccoroutine:mccoroutine-bungeecord-core:${Versions.MC_COROUTINE}")
    library("io.lettuce:lettuce-core:6.2.0.RELEASE")
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}")
    library(kotlin("stdlib-jdk8"))
    library(kotlin("reflect"))
}

bungee {
    name = thisPluginName
    main = "$${rootProject.group}.$directoryName.$thisPluginName" + "Plugin"
    author = "HighRight"
    depends = setOf(pluginName)
}