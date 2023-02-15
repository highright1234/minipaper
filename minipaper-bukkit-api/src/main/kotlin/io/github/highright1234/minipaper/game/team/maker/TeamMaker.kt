package io.github.highright1234.minipaper.game.team.maker

import io.github.highright1234.minipaper.game.team.GameTeam
import java.util.*

interface TeamMaker {

    fun makeTeam(
        teamSize: Int,
        players : Collection<UUID>,
        scores: Map<UUID, Int>? = null
    ) : Collection<GameTeam>

}