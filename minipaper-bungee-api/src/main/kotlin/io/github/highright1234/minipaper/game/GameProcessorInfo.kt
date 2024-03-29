package io.github.highright1234.minipaper.game

import net.md_5.bungee.api.config.ServerInfo
import java.util.UUID

interface GameProcessorInfo {
    val game: GameInfo
    val uniqueId: UUID
    val owner: ServerInfo
}