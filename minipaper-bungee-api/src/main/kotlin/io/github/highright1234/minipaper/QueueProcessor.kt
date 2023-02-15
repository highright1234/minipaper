package io.github.highright1234.minipaper

import io.github.highright1234.minipaper.game.GameInfo
import net.md_5.bungee.api.connection.ProxiedPlayer

interface QueueProcessor {
    suspend fun take(gameInfo: GameInfo, size: Int = 1): List<ProxiedPlayer>
    suspend fun getQueuePlayers(gameInfo: GameInfo): List<ProxiedPlayer>
    suspend fun addPlayer(gameInfo: GameInfo, player: ProxiedPlayer)
    suspend fun removePlayer(player: ProxiedPlayer)
}
