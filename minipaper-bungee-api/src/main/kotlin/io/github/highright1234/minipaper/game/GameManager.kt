package io.github.highright1234.minipaper.game

import net.md_5.bungee.api.config.ServerInfo

interface GameManager {
    val games : Map<String, GameInfo>
    val gameProcessors : Map<ServerInfo, GameProcessorInfo>
}