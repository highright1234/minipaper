package io.github.highright1234.minipaper.coroutine

import io.github.highright1234.minipaper.game.GameProcessor
import kotlinx.coroutines.CoroutineScope
import org.bukkit.plugin.java.JavaPlugin

interface CoroutineManager {
    fun synchronousScopeOf(plugin : JavaPlugin) : CoroutineScope
    fun synchronousScopeOf(gameProcessor: GameProcessor) : CoroutineScope
    fun coroutineScopeOf(plugin: JavaPlugin) : CoroutineScope
    fun coroutineScopeOf(gameProcessor: GameProcessor) : CoroutineScope
}