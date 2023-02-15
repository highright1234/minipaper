package io.github.highright1234.minipaper.game

import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.PriorityBy
import org.bukkit.plugin.java.JavaPlugin


fun Iterable<GameProcessor>.pickOne(): GameProcessor {
    val list = shuffled().filter { it.isJoinable }
    return when (MiniPaper.gameManager.priorityBy) {
        PriorityBy.MANY_PLAYERS -> {
            list.maxBy { it.players.size }
        }
        PriorityBy.FEW_PLAYERS -> {
            list.maxBy { it.players.size }
        }
        PriorityBy.RANDOM -> {
            list.first()
        }
    }
}

interface GameManager {

    var priorityBy: PriorityBy

    suspend fun register(gameInfo: GameInfo, gameProcessorClass : Class<out GameProcessor>)
    suspend fun createProcessor(gameInfo: GameInfo)

    val games : Map<String, GameInfo>
    val gameProcessors: Map<GameInfo, Collection<GameProcessor>>


    fun getGameDatastore(gameInfo: GameInfo, property: String): GameDatastore

}