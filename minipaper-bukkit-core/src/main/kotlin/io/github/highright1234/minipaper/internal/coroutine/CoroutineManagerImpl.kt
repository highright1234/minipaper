package io.github.highright1234.minipaper.internal.coroutine

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.github.shynixn.mccoroutine.bukkit.scope
import com.google.common.collect.MapMaker
import io.github.highright1234.minipaper.MinipaperPlugin.Companion.plugin
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.coroutine.CoroutineManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext

object CoroutineManagerImpl : CoroutineManager {

    private val scopes = MapMaker().weakKeys().makeMap<GameProcessor, CoroutineScope>()
    private val mcDispatchers = MapMaker().weakKeys().makeMap<GameProcessor, CoroutineContext>()
    private val asyncDispatchers = MapMaker().weakKeys().makeMap<GameProcessor, CoroutineContext>()

    override fun scopeOf(gameProcessor: GameProcessor): CoroutineScope =
        scopes.getOrPut(gameProcessor) { plugin.scope + SupervisorJob() }

    override fun minecraftDispatcherOf(gameProcessor: GameProcessor): CoroutineContext =
        mcDispatchers.getOrPut(gameProcessor) { plugin.minecraftDispatcher + SupervisorJob() }

    override fun asyncDispatcherOf(gameProcessor: GameProcessor): CoroutineContext =
        asyncDispatchers.getOrPut(gameProcessor) { plugin.asyncDispatcher + SupervisorJob() }

}