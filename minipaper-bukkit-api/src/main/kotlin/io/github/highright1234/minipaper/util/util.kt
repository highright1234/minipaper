package io.github.highright1234.minipaper.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

/**
 * Simple converter for uuid list to player list
 */
val Iterable<UUID>.onlinePlayers: List<Player> get() = mapNotNull { Bukkit.getPlayer(it) }

