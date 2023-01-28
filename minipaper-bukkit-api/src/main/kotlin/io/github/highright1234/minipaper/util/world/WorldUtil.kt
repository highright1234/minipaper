package io.github.highright1234.minipaper.util.world

import org.bukkit.World

interface WorldUtil {
    suspend fun cloneWorld(origin: String, newWorld: String): World

    suspend fun unloadWorld(world: World, save: Boolean = false)

    suspend fun deleteWorld(world: String)

    suspend fun loadWorld(name: String): World
}