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
    mavenCentral()
}

dependencies {
    monunLibrary("tap", Versions.TAP)
    monunLibrary("invfx", Versions.INVFX)
    monunLibrary("kommand", Versions.KOMMAND)
    bukkitLibrary("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:${Versions.MC_COROUTINE}")
    bukkitLibrary("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:${Versions.MC_COROUTINE}")
//    bukkitLibrary("io.github.highright1234:shotokonoko:${Versions.SHOTOKONOKO}")
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}")
    library("io.lettuce:lettuce-core:6.2.0.RELEASE")
    library(kotlin("stdlib-jdk8"))
    library(kotlin("reflect"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("io.mockk:mockk:1.12.7")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    testImplementation("org.slf4j:slf4j-simple:2.0.0")
}

val directoryName = rootProject.name.replace("-", "")
val pluginName = rootProject.name.split("-").joinToString(separator = "") { it.capitalizeAsciiOnly() }

bukkit {
    name = pluginName
    main = "${rootProject.group}.$directoryName.$pluginName" + "Plugin"
    author = "HighRight"
}