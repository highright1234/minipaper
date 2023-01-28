package io.github.highright1234.minipaper.kommand.processor

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.kommand.SubKommand
import io.github.highright1234.shotokonoko.monun.suspendingExecutes
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit

object Team: SubKommand {
    override fun init(node: KommandNode) {
        node.then("team") {
            suspendingExecutes { context ->
                val processor: GameProcessor by context
                processor.team
                    .map { it.bukkitTeam.name to it.players }
                    .joinToString(
                        prefix = "",
                        postfix = "",
                        separator = "\n",
                    ) { (name, players) ->
                        "$name: ${players.joinToString { Bukkit.getPlayer(it)?.name ?: it.toString() }}"
                    }.let(Component::text).let(player::sendMessage)
            }
        }
    }
}