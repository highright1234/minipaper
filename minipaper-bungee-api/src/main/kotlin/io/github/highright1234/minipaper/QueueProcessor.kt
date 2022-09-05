package io.github.highright1234.minipaper

import io.github.highright1234.minipaper.game.GameInfo
import net.md_5.bungee.api.connection.ProxiedPlayer

interface QueueProcessor {
    fun take(gameInfo: GameInfo, size: Int = 1): List<ProxiedPlayer>
    fun getQueuePlayers(gameInfo: GameInfo): List<ProxiedPlayer>
    fun addPlayer(gameInfo: GameInfo, player: ProxiedPlayer)
    fun removePlayer(player: ProxiedPlayer)
}
