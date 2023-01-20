package io.github.highright1234.minipaper.game

import org.bukkit.plugin.java.JavaPlugin

data class GameInfo(
    val name: String,
    val plugin: JavaPlugin,
    val version: String,
) {
    /**
     * make GameInfo with plugin's version
     */
    constructor(name: String, plugin: JavaPlugin) : this(name, plugin, plugin.description.version)
}
