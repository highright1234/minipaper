package io.github.highright1234.minipaper.game

import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.PriorityBy
import org.bukkit.plugin.java.JavaPlugin


fun Iterable<GameProcessor>.pickOne(): GameProcessor {
    val list = shuffled()
    return when (MiniPaper.gameManager.priorityBy) {
        PriorityBy.MANY_PLAYERS -> {
            list.maxByOrNull { it.players.size }!!
        }
        PriorityBy.FEW_PLAYERS -> {
            list.minByOrNull { it.players.size }!!
        }
        PriorityBy.RANDOM -> {
            list.first()
        }
    }
}

interface GameManager {

    var priorityBy: PriorityBy

    fun register(plugin: JavaPlugin, gameProcessorClass : Class<out GameProcessor>)
    val games : Map<String, GameInfo>
    val gameProcessors: Map<GameInfo, Collection<GameProcessor>>

}