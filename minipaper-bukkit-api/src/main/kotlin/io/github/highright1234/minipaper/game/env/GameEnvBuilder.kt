package io.github.highright1234.minipaper.game.env

import com.github.shynixn.mccoroutine.bukkit.scope
import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.event.processor.EnvPostCreateEvent
import io.github.highright1234.minipaper.event.processor.EnvPreCreateEvent
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.plugin
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.bukkit.Bukkit
import org.bukkit.World

object GameEnvBuilder {

    private val logger = plugin.logger

    fun addOwnership(owner: GameProcessor, vararg worlds: World) {
        owner._world += worlds
    }

    private val characters = ( '0'..'9' ) + ( 'A'..'Z' ) + ( 'a'..'z' )


    suspend fun create(owner: GameProcessor, vararg origins: String): List<Pair<String, World>> {
        val uniqueName = List(6) { characters.random() }.joinToString("")
        val newWorlds = origins
            .map {
                val name = "${uniqueName}_${it}"
                // keepSpawnLoaded 저거는 렉 줄여주는거
                it to name
            }
            .map { (origin, newWorldName) -> plugin.scope.async {
                val event = EnvPreCreateEvent(newWorldName, origin, owner)
                Bukkit.getPluginManager().callEvent(event)
                val world = event.world ?: run {
                    logger.info("Making $newWorldName from $origin")
                    MiniPaper.worldUtil.cloneWorld(origin, newWorldName)
                }
                origin to world
            } }
            .awaitAll()
            .onEach { (origin, world) ->
                addOwnership(owner, world)
                val event = EnvPostCreateEvent(world, origin, owner)
                Bukkit.getPluginManager().callEvent(event)
                logger.info("${world.name} is successfully created from ${origin}.")
            }
        return newWorlds
    }

}