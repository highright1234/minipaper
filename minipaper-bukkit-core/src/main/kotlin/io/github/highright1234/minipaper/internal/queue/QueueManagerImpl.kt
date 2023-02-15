package io.github.highright1234.minipaper.internal.queue

import com.google.common.collect.MapMaker
import io.github.highright1234.minipaper.event.queue.QueueJoinEvent
import io.github.highright1234.minipaper.event.queue.QueuePollEvent
import io.github.highright1234.minipaper.event.queue.QueueQuitEvent
import io.github.highright1234.minipaper.event.queue.QueueTakeEvent
import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.queue.QueueManager
import io.github.highright1234.shotokonoko.collections.newPlayerLinkedDeque
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayDeque

object QueueManagerImpl: QueueManager {

    private val lock = Any()
    private val queues = MapMaker().weakKeys().makeMap<GameInfo, Deque<Player>>()

    override fun queueOf(gameInfo: GameInfo): List<Player> = synchronized(lock) { queues[gameInfo]?.toList() ?: emptyList() }

    override fun addPlayer(player: Player, gameInfo: GameInfo) {
        synchronized(lock) {
            val queue = queues.getOrPut(gameInfo) { newPlayerLinkedDeque() }
            if (QueueJoinEvent(gameInfo, player).callEvent()) {
                queue.addLast(player)
            }
        }
    }

    override fun removePlayer(player: Player, gameInfo: GameInfo) {
        synchronized(lock) {
            val queue = queues[gameInfo] ?: return
            QueueQuitEvent(gameInfo, player).callEvent()
            queue.remove(player)
        }
    }

    override fun poll(gameInfo: GameInfo, n: Int): List<Player> {
        synchronized(lock) {
            val queue = queues[gameInfo] ?: return emptyList()
            val number = if (queue.size < n) queue.size else n
            val cloneOfQueue = ArrayDeque(queue)
            val expectedResult = List(number) { cloneOfQueue.removeFirst() }
            val event = QueuePollEvent(gameInfo, n, expectedResult).apply { callEvent() }
            return event.result.also { queue.removeAll(it.toSet()) }
        }
    }

    override fun take(gameInfo: GameInfo, n: Int): List<Player> {
        synchronized(lock) {
            val queue = queues[gameInfo] ?: return emptyList()
            val number = if (queue.size < n) queue.size else n
            val event = QueueTakeEvent(gameInfo, n, queue.take(number)).apply { callEvent() }
            return event.result
        }
    }
}