package io.github.highright1234.minipaper.coroutine

import io.github.highright1234.minipaper.game.GameProcessor
import kotlinx.coroutines.CoroutineScope
import org.bukkit.plugin.java.JavaPlugin
import kotlin.coroutines.CoroutineContext

interface CoroutineManager {
    fun scopeOf(gameProcessor: GameProcessor) : CoroutineScope
    fun minecraftDispatcherOf(gameProcessor: GameProcessor) : CoroutineContext
    fun asyncDispatcherOf(gameProcessor: GameProcessor) : CoroutineContext
}