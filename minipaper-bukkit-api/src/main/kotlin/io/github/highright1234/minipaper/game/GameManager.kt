package io.github.highright1234.minipaper.game

import org.bukkit.entity.Player

interface GameManager {
    fun addPlayer(player: Player, gameProcessor: GameProcessor)
    fun removePlayer(player: Player, gameProcessor: GameProcessor)
    fun addPlayer(player: Player, gameInfo: GameInfo)
    fun removePlayer(player: Player, gameInfo: GameInfo)
}