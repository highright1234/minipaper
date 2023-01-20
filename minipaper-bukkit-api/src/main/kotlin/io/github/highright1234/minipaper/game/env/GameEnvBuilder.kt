package io.github.highright1234.minipaper.game.env

import io.github.highright1234.minipaper.event.EnvCreatedEvent
import io.github.highright1234.minipaper.event.EnvCreatingEvent
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.plugin
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator

object GameEnvBuilder {

    private val logger = plugin.logger

    fun addOwnership(owner: GameProcessor, vararg worlds: World) {
        owner._world += worlds
    }

    private val characters = ( '0'..'9' ) + ( 'A'..'Z' ) + ( 'a'..'z' )
    fun create(owner: GameProcessor, vararg origins: World): List<Pair<World, World>> {
        val uniqueName = List(6) { characters.random() }.joinToString("")
        val newWorlds = origins
            .asSequence()
            .map {
                val name = "${uniqueName}_${it.name}"
                it to WorldCreator(name).copy(it)
            }
            .onEach { (world, creator) ->
                logger.info("Creating ${creator.name()} from ${world.name}")
            }
            .map { (world, creator) ->
                val event = EnvCreatingEvent(creator.name(), world, owner)
                Bukkit.getPluginManager().callEvent(event)
                world to (event.world ?: creator.createWorld()!!)
            }
            .onEach { (origin, world) ->
                addOwnership(owner, world)
                val event = EnvCreatedEvent(world, origin, owner)
                Bukkit.getPluginManager().callEvent(event)
                logger.info("${world.name} is successfully created from ${origin.name}.")
            }
        return newWorlds.toList()
    }

}