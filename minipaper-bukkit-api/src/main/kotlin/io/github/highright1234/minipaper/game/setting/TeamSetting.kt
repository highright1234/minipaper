package io.github.highright1234.minipaper.game.setting

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.game.team.maker.DefaultTeamMaker
import io.github.highright1234.minipaper.game.team.maker.TeamMakerProvider

data class TeamSetting(
    var teamSize: Int,
    var teamMakerProvider: TeamMakerProvider = DefaultTeamMaker.provider,
) {
    internal fun createTeam(processor: GameProcessor) {
        teamMakerProvider
            .makeTeamMaker(processor)
            .makeTeam(
                teamSize,
                processor.players,
            ).let { processor._team += it }
    }
}