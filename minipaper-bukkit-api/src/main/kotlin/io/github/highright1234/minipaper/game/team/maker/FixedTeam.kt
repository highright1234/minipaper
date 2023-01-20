package io.github.highright1234.minipaper.game.team.maker

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.game.team.DefaultTeam
import io.github.highright1234.minipaper.game.team.GameTeam
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun GameProcessor.fixedTeam(
    block: () -> Collection<Collection<Player>>
) = FixedTeam(this, block)

class FixedTeam(
    private val processor: GameProcessor,
    private val block: () -> Collection<Collection<Player>>
): TeamMaker {

    val scoreboard = Bukkit.getScoreboardManager().newScoreboard

    override fun makeTeam(
        teamSize: Int,
        players : Collection<Player>,
        scores: Map<Player, Int>?
    ): Collection<GameTeam> {
        val teamData = block().toList()
        return List(teamData.size) { i ->
            scoreboard.registerNewTeam(DefaultTeam.all[i].name)
                .apply {
                    teamData[i].forEach(::addPlayer)
                    color(DefaultTeam.all[i].color)
                }
        }.map {
            GameTeam(processor, it)
        }
    }
}