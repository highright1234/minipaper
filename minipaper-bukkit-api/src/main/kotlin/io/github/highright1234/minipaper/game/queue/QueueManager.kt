package io.github.highright1234.minipaper.game.queue

import io.github.highright1234.minipaper.game.GameInfo
import org.bukkit.entity.Player

interface QueueManager {

    fun queueOf(gameInfo: GameInfo): List<Player>

    fun addPlayer(player: Player, gameInfo: GameInfo)

    fun removePlayer(player: Player, gameInfo: GameInfo)

    /**
     * pull players from a queue
     * if queue is smaller than numberOfElement
     * it will return what it could
     *
     * @return players in queue
     */
    fun poll(gameInfo: GameInfo, n: Int = 1): List<Player>

    /**
     * pull players from a queue
     * if queue is smaller than numberOfElement
     * it will return what it could
     *
     * @return players in queue
     */
    fun take(gameInfo: GameInfo, n: Int = 1): List<Player>
}