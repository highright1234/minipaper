package io.github.highright1234.minipaper.internal.game

import io.github.highright1234.minipaper.game.GameManager
import io.github.highright1234.minipaper.game.GameProcessorInfo
import net.md_5.bungee.api.config.ServerInfo

object GameManagerImpl: GameManager {
    override val gameProcessors: Map<ServerInfo, GameProcessorInfo> = emptyMap()
}