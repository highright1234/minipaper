package io.github.highright1234.minipaper

import io.github.highright1234.minipaper.bungee.PluginMessageUtil
import io.github.highright1234.minipaper.coroutine.CoroutineManager
import io.github.highright1234.minipaper.game.event.GameEventManager
import io.github.highright1234.minipaper.game.GameProcessor
import org.bukkit.plugin.java.JavaPlugin

interface MiniPaper {
    companion object: MiniPaper by loader()
    val runningGameProcessor: GameProcessor?
    val eventManger: GameEventManager
    val coroutineManager: CoroutineManager
    val pluginMessageUtil: PluginMessageUtil
    fun register(plugin: JavaPlugin, gameProcessorClass : Class<out GameProcessor>)
}

private fun loader() =
    MiniPaper::class.java.`package`
        .let { "${it.name}.internal.MiniPaperImpl" }
        .let { Class.forName(it) }
        .getField("INSTANCE")[null] as MiniPaper