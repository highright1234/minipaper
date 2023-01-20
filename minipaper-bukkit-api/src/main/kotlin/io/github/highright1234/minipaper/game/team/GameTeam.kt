package io.github.highright1234.minipaper.game.team

import io.github.highright1234.minipaper.game.GameProcessor
import org.bukkit.scoreboard.Team
import java.util.*

class GameTeam(
    val owner: GameProcessor,
    val bukkitTeam: Team
) {
    var players: Collection<UUID> = listOf()
}