package io.github.highright1234.shotokonoko

import io.github.highright1234.shotokonoko.internal.ShotokonokoImpl
import io.github.highright1234.shotokonoko.internal.bungee.PluginMessageUtilImpl
import io.github.highright1234.shotokonoko.kommand.ShotokonokoKommand
import io.github.monun.kommand.kommand
import org.bukkit.plugin.java.JavaPlugin
import java.io.BufferedReader
import java.io.FileReader
import java.util.*


class ShotokonokoPlugin : JavaPlugin() {
    companion object {
        private lateinit var plugin_: JavaPlugin
        val plugin get() = plugin_
    }
    override fun onEnable() {
        ShotokonokoImpl.plugin = this
        plugin_ = this
        kommand {
            ShotokonokoKommand.register(this)
        }
        if (!isOnlineMode && isBungee) {
            for (i in 1..10) {
                logger.warning("Bungeecord setting can't be with online-mode")
            }
            pluginLoader.disablePlugin(this)
        } else if (isBungee) {
            ShotokonokoImpl.pluginMessageUtil = PluginMessageUtilImpl
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