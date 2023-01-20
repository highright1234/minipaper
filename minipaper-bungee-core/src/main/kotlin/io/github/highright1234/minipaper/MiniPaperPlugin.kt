package io.github.highright1234.minipaper

import io.github.highright1234.minipaper.pluginmessage.PluginMessageListener
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File

class MiniPaperPlugin : Plugin() {
    companion object {
        lateinit var config : Configuration
    }
    private val listeners : List<Listener> = listOf(
        PluginMessageListener
    )
    override fun onEnable() {
        listeners.forEach { proxy.pluginManager.registerListener(this, it) }
        TODO()
    }

    private fun loadData() : Configuration {
        return ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(
            File(dataFolder, "config.yml")
        )
    }

    fun saveConfig() {
        ConfigurationProvider.getProvider(YamlConfiguration::class.java).save(
            config, File(dataFolder, "config.yml")
        )
    }
}