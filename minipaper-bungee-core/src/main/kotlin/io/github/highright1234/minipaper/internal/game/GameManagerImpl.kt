package io.github.highright1234.minipaper.internal.game

import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameManager
import io.github.highright1234.minipaper.game.GameProcessorInfo
import net.md_5.bungee.api.config.ServerInfo

object GameManagerImpl: GameManager {

    override val games: Map<String, GameInfo>
    get() = gameProcessors
        .map { (_, processor) -> processor.game }.toSet()
        .associateBy { it.name }
    override val gameProcessors: Map<ServerInfo, GameProcessorInfo> = emptyMap()

    private val processorsOfGameInfo: MutableMap<GameInfoImpl, MutableList<GameProcessorInfoImpl>> = mutableMapOf()
    internal fun processorsOf(gameInfoImpl: GameInfoImpl) : Collection<GameProcessorInfoImpl> =
        synchronized(this) {
            processorsOfGameInfo.getOrElse(gameInfoImpl) { mutableListOf() }
        }
}