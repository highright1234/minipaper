package io.github.highright1234.minipaper.debug

import io.github.highright1234.minipaper.MiniPaper
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class MiniPaperDebug : JavaPlugin() {
    override fun onEnable() {
        MiniPaper.register(this, TestGameProcessor::class.java)
        MiniPaper.runningGameProcessor?.let {
            it += Bukkit.getOnlinePlayers()
        }

    }
}