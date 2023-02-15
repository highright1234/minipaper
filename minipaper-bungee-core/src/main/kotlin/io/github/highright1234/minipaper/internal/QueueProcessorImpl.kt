package io.github.highright1234.minipaper.internal

import io.github.highright1234.minipaper.QueueProcessor
import io.github.highright1234.minipaper.game.GameInfo
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.md_5.bungee.api.connection.ProxiedPlayer

object QueueProcessorImpl: QueueProcessor {
    private val mutex = Mutex()
    private val queues = mutableMapOf<GameInfo, List<ProxiedPlayer>>()
    private val queueMap = mutableMapOf<ProxiedPlayer, GameInfo>()

    override suspend fun take(gameInfo: GameInfo, size: Int): List<ProxiedPlayer> = mutex.withLock {
        queues
            .getOrDefault(gameInfo, emptyList())
            .take(size)
            .also { list ->
                queues[gameInfo] = list
                list.forEach { removePlayer(it) }
            }
    }

    override suspend fun getQueuePlayers(gameInfo: GameInfo): List<ProxiedPlayer> =
        mutex.withLock { queues.getOrDefault(gameInfo, ArrayDeque()) }

    override suspend fun addPlayer(gameInfo: GameInfo, player: ProxiedPlayer) {
        mutex.withLock {
            val queue = queues.getOrDefault(gameInfo, emptyList()).plus(player)
            queues[gameInfo] = queue
            queueMap[player] = gameInfo
        }
    }

    override suspend fun removePlayer(player: ProxiedPlayer) {
        mutex.withLock {
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