package io.github.highright1234.minipaper.game.team

import org.bukkit.entity.Player

interface TeamMaker {
    var players : Collection<Player>
    var scores: Map<Player, Int>?
    fun makeTeam(teamSize: Int) : Collection<GameTeam>
}