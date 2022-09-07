package io.github.highright1234.minipaper.internal.coroutine

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.coroutine.CoroutineManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import org.bukkit.plugin.java.JavaPlugin

object CoroutineManagerImpl : CoroutineManager {

    var syncScopeOfPlugin = mapOf<JavaPlugin, CoroutineScope>()
    private set
    var syncScopeOfGameProcessor = mapOf<GameProcessor, CoroutineScope>()
    private set

    var scopeOfPlugin = mapOf<JavaPlugin, CoroutineScope>()
        private set
    var scopeOfGameProcessor = mapOf<GameProcessor, CoroutineScope>()
        private set

    override fun synchronousScopeOf(plugin : JavaPlugin) : CoroutineScope {
        syncScopeOfPlugin[plugin]?.let { return it }
        val supervisorJob = SupervisorJob(SynchronousDispatcher.job)
        val scope = CoroutineScope(supervisorJob)
        syncScopeOfPlugin
            .plus(plugin to scope)
            .also { syncScopeOfPlugin = it }
        return scope
    }

    override fun synchronousScopeOf(gameProcessor: GameProcessor) : CoroutineScope {
        syncScopeOfGameProcessor[gameProcessor]?.let { return it }
        val parentScope = synchronousScopeOf(gameProcessor.plugin)
        val supervisorJob = SupervisorJob(parentScope.coroutineContext.job)
        val scope = CoroutineScope(supervisorJob)
        syncScopeOfGameProcessor
            .plus(gameProcessor to scope)
            .also { syncScopeOfGameProcessor = it }
        return scope
    }

    override fun coroutineScopeOf(plugin: JavaPlugin): CoroutineScope {
        scopeOfPlugin[plugin]?.let { return it }
        val supervisorJob = SupervisorJob()
        val scope = CoroutineScope(supervisorJob)
        scopeOfPlugin
            .plus(plugin to scope)
            .also { scopeOfPlugin = it }
        return scope
    }

    override fun coroutineScopeOf(gameProcessor: GameProcessor): CoroutineScope {
        scopeOfGameProcessor[gameProcessor]?.let { return it }
        val supervisorJob = SupervisorJob()
        val scope = CoroutineScope(supervisorJob)
        scopeOfGameProcessor
            .plus(gameProcessor to scope)
            .also { scopeOfGameProcessor = it }
        return scope
    }

}