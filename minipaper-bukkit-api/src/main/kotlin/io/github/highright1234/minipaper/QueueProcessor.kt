package io.github.highright1234.minipaper

import org.bukkit.entity.Player

abstract class QueueProcessor {
    abstract fun addPlayer(player: Player)
    abstract fun removePlayer(player: Player)
}