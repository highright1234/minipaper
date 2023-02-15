package io.github.highright1234.minipaper.game.team.maker

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.game.team.DefaultTeam
import io.github.highright1234.minipaper.game.team.GameTeam
import org.bukkit.Bukkit
import java.util.*

class DefaultTeamMaker(
    private val processor: GameProcessor,
) : TeamMaker {

    companion object {
        val provider = object : TeamMakerProvider {
            override fun makeTeamMaker(gameProcessor: GameProcessor): TeamMaker = DefaultTeamMaker(gameProcessor)
        }
    }

    val scoreboard = Bukkit.getScoreboardManager().newScoreboard

    /**
     * @param teamSize 팀 개수
     */
    override fun makeTeam(
        teamSize: Int,
        players : Collection<UUID>,
        scores: Map<UUID, Int>?
    ): Collection<GameTeam> {
        if (teamSize == 0) return emptyList()
        if (teamSize == 1)
            return scoreboard.registerNewTeam(DefaultTeam.all[0].name)
                .apply {
                    color(DefaultTeam.all[0].color)
                    players.mapNotNull { Bukkit.getPlayer(it) }.forEach(::addPlayer)
                }
                .let { team -> GameTeam(processor, team).also { it.players = players } }
                .let { listOf(it) }
        val playerList = players.toList()
        if (teamSize >= players.size) return List(players.size) { i ->
            scoreboard.registerNewTeam(DefaultTeam.all[i].name)
                .apply {
                    color(DefaultTeam.all[i].color)
                    playerList[i].let(Bukkit::getPlayer)?.let(::addPlayer)
                }
                .let { team ->
                    GameTeam(processor, team).also { it.players = listOf(playerList[i]) }
                }
        }

        return scores?.let { scoreTeam(teamSize, players, it) } ?: randomTeam(teamSize, players)
    }

    private fun scoreTeam(
        teamSize: Int,
        players : Collection<UUID>,
        scores: Map<UUID, Int>
    ): Collection<GameTeam> {
        val sorted = players.sortedByDescending { scores.getValue(it) }.toMutableList()
        val teams = MutableList<ArrayList<UUID>>(teamSize) { arrayListOf() }
        var highPosition = true
        var index = 0
        fun put() {
            val team = teams[index]
            if (highPosition) {
                team.add(sorted.removeFirst())
            } else {
                team.add(sorted.removeLast())
            }
        }
        while (sorted.isNotEmpty()) {
            if (index == teamSize) {
                index = 0
                highPosition = false
            }
            put()
            index++
        }


        val formatted = teams.mapIndexed { i, members ->
            scoreboard.registerNewTeam(DefaultTeam.all[i].name)
                .also { team ->
                    members.mapNotNull { Bukkit.getPlayer(it) }.forEach(team::addPlayer)
                    team.color(DefaultTeam.all[i].color)
                }
                .let { team ->
                    GameTeam(processor, team).also { it.players = members }
                }
        }

        return formatted
    }

    private fun randomTeam(teamSize: Int, players : Collection<UUID>): Collection<GameTeam> =
        players.shuffled()
            .mapIndexed { index, player -> index to player }
            .groupBy { it.first % teamSize }
            .values.map { pair -> pair.map { it.second } }
            .mapIndexed { index, members ->
                GameTeam(processor, scoreboard.registerNewTeam("$index")).also {
                    it.players = members
                }
            }
}