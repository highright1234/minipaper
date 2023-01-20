package io.github.highright1234.minipaper.util.scoreboard

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class SmartScoreboard(private val initCode: ScoreboardMeta.() -> Unit) {
    fun update(vararg players: Player) {
        players
            .map(::ScoreboardMeta)
            .onEach { it.apply() }
    }

    companion object {
        fun resetPlayer(vararg players: Player) {
            players.forEach {
                it.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
            }
        }
    }
}