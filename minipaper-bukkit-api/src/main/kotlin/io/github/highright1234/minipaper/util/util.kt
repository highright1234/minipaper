package io.github.highright1234.minipaper.util

import org.bukkit.Bukkit
import java.util.*

val Iterable<UUID>.onlinePlayers get() =
    filter { Bukkit.getPlayer(it) != null }
        .map { Bukkit.getPlayer(it)!! }