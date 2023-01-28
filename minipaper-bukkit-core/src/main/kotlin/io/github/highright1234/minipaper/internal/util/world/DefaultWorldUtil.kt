package io.github.highright1234.minipaper.internal.util.world

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import io.github.highright1234.minipaper.internal.MiniPaperImpl.plugin
import io.github.highright1234.minipaper.util.world.WorldUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kyori.adventure.util.TriState
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import java.io.File
import java.io.IOException

object DefaultWorldUtil: WorldUtil {
    suspend fun cloneWorldFile(origin: File, newWorld: File) {
        withContext(Dispatchers.IO) {
            origin.copyRecursively(newWorld) { _, _ -> OnErrorAction.SKIP }
            File(newWorld, "uid.dat").delete()
        }
    }

    override suspend fun cloneWorld(origin: String, newWorld: String): World {
        val worldContainer = Bukkit.getWorldContainer()
        val worldFolder = File(worldContainer, origin)
        val newWorldFolder = File(worldContainer, newWorld)
        require(!newWorldFolder.exists()) { "$newWorld is already exist!" }
        val world = withContext(plugin.minecraftDispatcher) {
            Bukkit.getWorld(origin)?.save()
            try {
                cloneWorldFile(worldFolder, newWorldFolder)
                loadWorld(newWorld)
            } catch (e: IOException) {
                WorldCreator(newWorld).apply {
                    keepSpawnLoaded(TriState.FALSE)
                    copy(Bukkit.getWorld(origin)!!)
                }.createWorld()!!
            }
        }
        return world
    }

    override suspend fun unloadWorld(world: World, save: Boolean) {
        Bukkit.unloadWorld(world, save)
    }

    override suspend fun deleteWorld(world: String) {
        require(Bukkit.getWorld(world) != null) { "World must be unloaded!" }
        File(Bukkit.getWorldContainer(), world).delete()
    }

    override suspend fun loadWorld(name: String): World {
        return WorldCreator(name).apply { keepSpawnLoaded(TriState.FALSE) }.createWorld()!!
    }
}