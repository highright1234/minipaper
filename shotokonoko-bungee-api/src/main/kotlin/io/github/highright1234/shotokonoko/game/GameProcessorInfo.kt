package io.github.highright1234.shotokonoko.game

import net.md_5.bungee.api.config.ServerInfo
import java.util.UUID

interface GameProcessorInfo {
    val game: GameInfo
    val uuid: UUID
    val owner: ServerInfo
}