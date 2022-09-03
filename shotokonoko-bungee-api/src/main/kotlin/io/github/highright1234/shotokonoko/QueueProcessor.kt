package io.github.highright1234.shotokonoko

import io.github.highright1234.shotokonoko.game.GameInfo
import net.md_5.bungee.api.connection.ProxiedPlayer

interface QueueProcessor {
    fun take(gameInfo: GameInfo, size: Int = 1): List<ProxiedPlayer>
    fun getQueuePlayers(gameInfo: GameInfo): List<ProxiedPlayer>
    fun addPlayer(gameInfo: GameInfo, player: ProxiedPlayer)
    fun removePlayer(player: ProxiedPlayer)
}
