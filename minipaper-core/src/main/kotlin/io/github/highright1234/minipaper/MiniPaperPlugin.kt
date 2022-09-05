package io.github.highright1234.minipaper

import io.github.highright1234.minipaper.internal.MiniPaperImpl
import io.github.highright1234.minipaper.kommand.MiniPaperKommand
import io.github.monun.kommand.kommand
import org.bukkit.plugin.java.JavaPlugin
import java.io.BufferedReader
import java.io.FileReader
import java.util.*


class MiniPaperPlugin : JavaPlugin() {
    companion object {
        private lateinit var plugin_: JavaPlugin
        val plugin get() = plugin_
    }
    override fun onEnable() {
        MiniPaperImpl.plugin = this
        plugin_ = this
        kommand {
            MiniPaperKommand.register(this)
        }
        if (!isOnlineMode && isBungee) {
            for (i in 1..10) {
                logger.warning("Bungeecord setting can't be with online-mode")
            }
            pluginLoader.disablePlugin(this)
            return
        }

    }

    private val isBungee get() = server.spigot().spigotConfig.getBoolean("settings.bungeecord")
    private val isOnlineMode get() = properties.getProperty("online-mode").toBoolean()

    private val properties : Properties get() {
        val props = Properties()
        val reader = BufferedReader(FileReader("server.properties"))
        props.load(reader)
        reader.close()
        return props
    }
}