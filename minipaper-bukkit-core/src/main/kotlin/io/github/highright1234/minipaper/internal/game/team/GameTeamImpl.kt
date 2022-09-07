package io.github.highright1234.minipaper.internal.game.team

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.game.team.GameTeam
import org.bukkit.scoreboard.Team
import java.util.*

class GameTeamImpl(
    override val owner: GameProcessor,
    override val bukkitTeam: Team
) : GameTeam {
    override var players: Collection<UUID> = listOf()
}