package io.github.highright1234.minipaper.internal.coroutine

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.coroutine.CoroutineManager
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

object CoroutineManagerImpl : CoroutineManager {
    private val scopes = hashMapOf<GameProcessor, CoroutineScope>()
    private val mcDispatchers = hashMapOf<GameProcessor, CoroutineContext>()
    private val asyncDispatchers = hashMapOf<GameProcessor, CoroutineContext>()
    override fun scopeOf(gameProcessor: GameProcessor): CoroutineScope {
        TODO("Not yet implemented")
    }

    override fun minecraftDispatcherOf(gameProcessor: GameProcessor): CoroutineContext {
        TODO("Not yet implemented")
    }

    override fun asyncDispatcherOf(gameProcessor: GameProcessor): CoroutineContext {
        TODO("Not yet implemented")
    }


}