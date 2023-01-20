package io.github.highright1234.minipaperdebug

import io.github.highright1234.minipaper.MiniPaper
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class MinipaperDebugPlugin : JavaPlugin() {
    companion object {
        lateinit var plugin : JavaPlugin
    }
    override fun onEnable() {
        plugin = this
        MiniPaper.register(this, TestGameProcessor::class.java)
        MiniPaper.runningGameProcessor?.let {
            it += Bukkit.getOnlinePlayers()
        }
    }
}