package io.github.highright1234.minipaper.util.scoreboard

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Criteria

class ScoreboardMeta(val player: Player) {
    private val strings = mutableListOf<String>()
    var displayName: Component? = null
    operator fun String.unaryPlus() { strings += this }
    operator fun Component.unaryPlus() = +string
    private val Component.string get() = LegacyComponentSerializer.legacySection().serialize(this)

    fun apply() {
        val scoreboard = Bukkit.getScoreboardManager()
            .newScoreboard
        val objective = scoreboard.registerNewObjective("scoreboard", Criteria.DUMMY, displayName)
        var score = strings.size
        strings.forEach {
            objective.getScore(it).score = score--
        }
        player.scoreboard = scoreboard
    }
}