package io.github.highright1234.minipaper.util

import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.game.pickOne
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

/**
 * Simple converter for uuid list to player list
 */
val Iterable<UUID>.onlinePlayers: List<Player> get() = mapNotNull { Bukkit.getPlayer(it) }

fun Player.joinGame(gameProcessor: GameProcessor) {
    gameProcessor.addPlayer(this)
}

fun Player.joinGame(gameInfo: GameInfo) {
    joinGame(gameInfo.processors.pickOne())
}

val Player.processor: GameProcessor? get() = MiniPaper.gameManager.gameProcessors.values.flatten().find { this in it }