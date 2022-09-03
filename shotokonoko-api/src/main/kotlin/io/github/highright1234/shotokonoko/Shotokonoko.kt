package io.github.highright1234.shotokonoko

import io.github.highright1234.shotokonoko.bungee.PluginMessageUtil
import io.github.highright1234.shotokonoko.coroutine.CoroutineManager
import io.github.highright1234.shotokonoko.game.event.GameEventManager
import io.github.highright1234.shotokonoko.game.GameProcessor
import org.bukkit.plugin.java.JavaPlugin

interface Shotokonoko {
    companion object: Shotokonoko by loader()
    val runningGameProcessor: GameProcessor?
    val eventManger: GameEventManager
    val coroutineManager: CoroutineManager
    val pluginMessageUtil: PluginMessageUtil
    fun register(plugin: JavaPlugin, gameProcessorClass : Class<out GameProcessor>)
}

private fun loader() =
    Shotokonoko::class.java.`package`
        .let { "${it.name}.internal.ShotokonokoImpl" }
        .let { Class.forName(it) }
        .getField("INSTANCE")[null] as Shotokonoko