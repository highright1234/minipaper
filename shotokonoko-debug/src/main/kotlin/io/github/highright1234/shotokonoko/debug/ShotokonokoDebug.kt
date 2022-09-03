package io.github.highright1234.shotokonoko.debug

import io.github.highright1234.shotokonoko.Shotokonoko
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ShotokonokoDebug : JavaPlugin() {
    override fun onEnable() {
        Shotokonoko.register(this, TestGameProcessor::class.java)
        Shotokonoko.runningGameProcessor?.let {
            it += Bukkit.getOnlinePlayers()
        }

    }
}