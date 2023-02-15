package io.github.highright1234.minipaper

import com.infernalsuite.aswm.api.SlimePlugin
import io.github.highright1234.minipaper.config.MiniPaperConfig
import io.github.highright1234.minipaper.internal.MiniPaperImpl
import io.github.highright1234.minipaper.internal.util.world.DefaultWorldUtil
import io.github.highright1234.minipaper.internal.util.world.SlimeWorldUtil
import io.github.highright1234.minipaper.kommand.MiniPaperKommand
import io.github.highright1234.minipaper.pluginmessage.BungeePluginMessageListener
import io.github.highright1234.shotokonoko.bungee.MessageChannel
import io.github.monun.kommand.kommand
import io.github.monun.tap.config.ConfigSupport
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*


class MinipaperPlugin : JavaPlugin() {

    companion object {
        private lateinit var plugin_: MinipaperPlugin
        val plugin get() = plugin_
    }


    override fun onEnable() {
        MiniPaperImpl.init(this)
        loadConfigs()
        plugin_ = this
        kommand {
            MiniPaperKommand.register(this)
        }
        
        val isBungeeSetting = (!isOnlineMode && isBungee)

        if (MiniPaper.isDebug) {
            logger.info("activated as debug-mode")
        } else if (isBungeeSetting) {
            logger.info("activated as bungee-mode")
        } else {
            logger.info("activated as default-mode")
        }

        if ( isOnlineMode && isBungee ) {
            repeat(10) {
                logger.warning("Bungeecord setting can't be with online-mode")
            }
            pluginLoader.disablePlugin(this)
            return
        }

        if (isBungeeSetting) {
            val messageChannel = MessageChannel("minipaper:main")
            messageChannel.registerIncoming(BungeePluginMessageListener)
        }

        (Bukkit.getPluginManager().getPlugin("SlimeWorldManager") as SlimePlugin?)?.let { slimePlugin ->
            val slimeLoader = slimePlugin.getLoader(MiniPaperConfig.slimeLoader)
            MiniPaperImpl.worldUtil = SlimeWorldUtil(slimePlugin, slimeLoader)
        } ?: run {
            MiniPaperImpl.worldUtil = DefaultWorldUtil
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Papi.register()
        }
    }

    val isBungee get() = server.spigot().spigotConfig.getBoolean("settings.bungeecord")
    private val isOnlineMode get() = properties.getProperty("online-mode").toBoolean()

    private val properties : Properties get() {
        val props = Properties()
        BufferedReader(FileReader("server.properties")).use {
            props.load(it)
        }
        return props
    }

    private val configs = listOf(
        MiniPaperConfig to "config.yml",
    )

    private fun loadConfigs() = configs
        .map { (config, fileName) -> config to File(dataFolder, fileName) }
        .onEach { (_, file) ->
            if (!file.exists()) { saveResource(file.name, false) }
        }
        .forEach { (config, file) ->
            ConfigSupport.compute(config, file)
        }
}