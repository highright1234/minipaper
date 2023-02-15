package io.github.highright1234.minipaper.game.team.maker

import io.github.highright1234.minipaper.game.GameProcessor

interface TeamMakerProvider {
    fun makeTeamMaker(gameProcessor: GameProcessor): TeamMaker
}