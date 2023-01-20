package io.github.highright1234.minipaper.game.team.maker

import io.github.highright1234.minipaper.game.team.GameTeam
import org.bukkit.entity.Player

interface TeamMaker {

    fun makeTeam(
        teamSize: Int,
        players : Collection<Player>,
        scores: Map<Player, Int>? = null
    ) : Collection<GameTeam>

}