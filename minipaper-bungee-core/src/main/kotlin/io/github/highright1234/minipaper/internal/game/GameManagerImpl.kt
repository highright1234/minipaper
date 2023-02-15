package io.github.highright1234.minipaper.internal.game

import com.google.common.collect.MapMaker
import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameManager
import io.github.highright1234.minipaper.game.GameProcessorInfo
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.md_5.bungee.api.config.ServerInfo

object GameManagerImpl: GameManager {

    private val mutex = Mutex()
    override val games: Map<String, GameInfo>
    get() = gameProcessors
        .values.flatten()
        .map { it.game }.toSet()
        .associateBy { it.name }

    override val gameProcessors: MutableMap<ServerInfo, MutableCollection<GameProcessorInfo>> =
        MapMaker().weakKeys().makeMap()
    private val processorsOfGameInfo: MutableMap<GameInfo, MutableCollection<GameProcessorInfo>> =
        MapMaker().weakKeys().makeMap()

    suspend fun addGameProcessor(gameProcessorInfo: GameProcessorInfo) {
        mutex.withLock {
            processorsOf(gameProcessorInfo.owner) += gameProcessorInfo
            processorsOf(gameProcessorInfo.game) += gameProcessorInfo
        }
    }

    suspend fun removeGameProcessor(gameProcessorInfo: GameProcessorInfo) {
        mutex.withLock {
            val serverData = gameProcessors[gameProcessorInfo.owner]!!
            serverData -= gameProcessorInfo
            if (serverData.isEmpty()) gameProcessors -= gameProcessorInfo.owner
            val gamesData = processorsOfGameInfo[gameProcessorInfo.game]!!
            gamesData -= gameProcessorInfo
            if (gamesData.isEmpty()) gamesData -= gameProcessorInfo
        }
    }

    internal fun processorsOf(serverInfo: ServerInfo) : MutableCollection<GameProcessorInfo> =
        gameProcessors.getOrPut(serverInfo) { mutableListOf() }

    internal fun processorsOf(gameInfo: GameInfo) : MutableCollection<GameProcessorInfo> =
        processorsOfGameInfo.getOrPut(gameInfo) { mutableListOf() }

}