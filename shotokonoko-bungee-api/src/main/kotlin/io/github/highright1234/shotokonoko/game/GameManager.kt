package io.github.highright1234.shotokonoko.game

import net.md_5.bungee.api.config.ServerInfo

interface GameManager {
    val gameProcessors : Map<ServerInfo, GameProcessorInfo>
}