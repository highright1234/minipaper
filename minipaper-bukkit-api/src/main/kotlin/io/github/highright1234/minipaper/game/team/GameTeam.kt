package io.github.highright1234.minipaper.game.team

import io.github.highright1234.minipaper.game.GameProcessor
import org.bukkit.scoreboard.Team
import java.util.*

interface GameTeam {
    val owner : GameProcessor
    val players: Collection<UUID>
    val bukkitTeam: Team
}