package io.github.highright1234.shotokonoko.coroutine

import io.github.highright1234.shotokonoko.game.GameProcessor
import kotlinx.coroutines.CoroutineScope
import org.bukkit.plugin.java.JavaPlugin

interface CoroutineManager {
    fun synchronousScopeOf(plugin : JavaPlugin) : CoroutineScope
    fun synchronousScopeOf(gameProcessor: GameProcessor) : CoroutineScope
    fun coroutineScopeOf(plugin: JavaPlugin) : CoroutineScope
    fun coroutineScopeOf(gameProcessor: GameProcessor) : CoroutineScope
}