package io.github.highright1234.minipaper.internal.util.world

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.infernalsuite.aswm.api.SlimePlugin
import com.infernalsuite.aswm.api.loaders.SlimeLoader
import com.infernalsuite.aswm.api.world.SlimeWorld
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap
import io.github.highright1234.minipaper.internal.MiniPaperImpl
import io.github.highright1234.minipaper.util.world.WorldUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.Bukkit
import org.bukkit.World

class SlimeWorldUtil(private val slimePlugin: SlimePlugin, var slimeLoader: SlimeLoader): WorldUtil {
    override suspend fun cloneWorld(origin: String, newWorld: String): World {
        val properties = SlimePropertyMap()

        val originWorld: SlimeWorld = withContext(Dispatchers.IO) {
            slimePlugin.loadWorld(slimeLoader, origin, true, properties)
        }
        val world: SlimeWorld = withContext(MiniPaperImpl.plugin.asyncDispatcher) {
            originWorld.clone(newWorld).let(slimePlugin::loadWorld)
        }

        return Bukkit.getWorld(world.name)!!
    }

    override suspend fun unloadWorld(world: World, save: Boolean) {
        Bukkit.unloadWorld(world, save)
    }

    override suspend fun deleteWorld(world: String) {
        withContext(Dispatchers.IO) {
            slimeLoader.deleteWorld(world)
        }
    }

    override suspend fun loadWorld(name: String): World {
        val properties = SlimePropertyMap()
        lateinit var world: SlimeWorld
        withContext(Dispatchers.IO) {
            world = slimePlugin.loadWorld(slimeLoader, name, true, properties)
        }
        slimePlugin.loadWorld(world)
        return Bukkit.getWorld(name)!!
    }
}