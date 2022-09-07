package io.github.highright1234.minipaper.internal.game.team

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.game.team.GameTeam
import io.github.highright1234.minipaper.game.team.TeamMaker
import org.bukkit.entity.Player

class DefaultTeamMaker(
    private val processor: GameProcessor,
    override var players: Collection<Player>,
    override var scores: Map<Player, Int>? = null
) : TeamMaker {

    override fun makeTeam(teamSize: Int): Collection<GameTeam> {
        if (teamSize == 0) return emptyList()
        if (teamSize == 1) return listOf(GameTeamImpl(processor, processor.scoreboard.registerNewTeam("1")).apply {
            this@DefaultTeamMaker.players.forEach {
                bukkitTeam.addPlayer(it)
            }
        })
        if (teamSize == players.size) return players.mapIndexed { index, player ->
            GameTeamImpl(processor, processor.scoreboard.registerNewTeam("$index").apply {
                addPlayer(player)
            })
        }
        return scores?.let { scoreTeam(teamSize) } ?: randomTeam(teamSize)
    }

    private fun scoreTeam(teamSize: Int): Collection<GameTeam> {
        val sorted = players.sortedByDescending { scores!!.getValue(it) }.toMutableList()
        val teams = ArrayList<ArrayList<Player>>(teamSize).apply {
            for (i in 1..teamSize) { add(arrayListOf()) }
        }
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
        val formatted = teams.mapIndexed { i, players ->
            val bukkitTeam = processor.scoreboard.registerNewTeam("$i").also { team ->
                players.forEach { team.addPlayer(it) }
            }
            GameTeamImpl(processor, bukkitTeam)
        }
        return formatted
    }

    private fun randomTeam(teamSize: Int): Collection<GameTeam> =
        players.shuffled()
            .mapIndexed { index, player -> index to player }
            .groupBy { it.first % teamSize }
            .values.map { pair -> pair.map { it.second } }
            .mapIndexed { index, members ->
                GameTeamImpl(processor, processor.scoreboard.registerNewTeam("$index")).also {
                    it.players = members.map { player -> player.uniqueId }
                }
            }
}