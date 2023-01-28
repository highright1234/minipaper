package io.github.highright1234.minipaper.game

import io.github.highright1234.minipaper.MiniPaper
import org.bukkit.plugin.java.JavaPlugin

class GameInfo(
    val name: String,
    val plugin: JavaPlugin,
    val version: String,
) {
    /**
     * make GameInfo with plugin's version
     */
    constructor(name: String, plugin: JavaPlugin) : this(name, plugin, plugin.description.version)

    val processors: Iterable<GameProcessor> get() = MiniPaper.gameManager.gameProcessors[this] ?: emptyList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameInfo

        if (name != other.name) return false
        if (plugin != other.plugin) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + plugin.hashCode()
        return result
    }


}
