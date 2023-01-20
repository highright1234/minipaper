package io.github.highright1234.minipaper.internal

import io.github.highright1234.minipaper.QueueProcessor
import io.github.highright1234.minipaper.game.GameInfo
import net.md_5.bungee.api.connection.ProxiedPlayer

object QueueProcessorImpl: QueueProcessor {
    private val queues = mutableMapOf<GameInfo, List<ProxiedPlayer>>()
    private val queueMap = mutableMapOf<ProxiedPlayer, GameInfo>()

    override fun take(gameInfo: GameInfo, size: Int): List<ProxiedPlayer> = synchronized(this) {
        queues
            .getOrDefault(gameInfo, emptyList())
            .take(size)
            .also {
                queues[gameInfo] = it
                it.forEach(::removePlayer)
            }
    }

    override fun getQueuePlayers(gameInfo: GameInfo): List<ProxiedPlayer> =
        synchronized(this) { queues.getOrDefault(gameInfo, ArrayDeque()) }

    override fun addPlayer(gameInfo: GameInfo, player: ProxiedPlayer) {
        synchronized(this) {
            val queue = queues.getOrDefault(gameInfo, emptyList()).plus(player)
            queues[gameInfo] = queue
            queueMap[player] = gameInfo
        }
    }

    override fun removePlayer(player: ProxiedPlayer) {
        synchronized(this) {
            queueMap[player] ?: return
            val gameInfo = queueMap[player]!!
            val queue = queues.getValue(gameInfo).minus(player)
            if (queue.isEmpty()) {
                queues -= gameInfo
            } else {
                queues[gameInfo] = queue
            }
            queueMap -= player
        }
    }
}