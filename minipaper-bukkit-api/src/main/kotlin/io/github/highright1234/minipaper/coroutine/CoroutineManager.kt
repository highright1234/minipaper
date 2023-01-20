package io.github.highright1234.minipaper.coroutine

import io.github.highright1234.minipaper.game.GameProcessor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface CoroutineManager {
    fun scopeOf(gameProcessor: GameProcessor) : CoroutineScope
    fun minecraftDispatcherOf(gameProcessor: GameProcessor) : CoroutineContext
    fun asyncDispatcherOf(gameProcessor: GameProcessor) : CoroutineContext

}