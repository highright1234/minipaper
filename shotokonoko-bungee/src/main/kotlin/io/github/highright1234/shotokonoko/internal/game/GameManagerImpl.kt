package io.github.highright1234.shotokonoko.internal.game

import io.github.highright1234.shotokonoko.game.GameManager
import io.github.highright1234.shotokonoko.game.GameProcessorInfo
import net.md_5.bungee.api.config.ServerInfo

object GameManagerImpl: GameManager {
    override val gameProcessors: Map<ServerInfo, GameProcessorInfo> = emptyMap()
}