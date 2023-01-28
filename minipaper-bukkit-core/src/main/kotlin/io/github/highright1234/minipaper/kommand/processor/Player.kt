package io.github.highright1234.minipaper.kommand.processor

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.kommand.SubKommand
import io.github.highright1234.shotokonoko.monun.suspendingExecutes
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import org.bukkit.entity.Player

object Player: SubKommand {
    override fun init(node: KommandNode) {
        node.then("player") {
            then("add") {
                then("players" to players()) {
                    suspendingExecutes { context ->
                        val processor: GameProcessor by context
                        val players: Collection<Player> by context
                        players.forEach(processor::addPlayer)
                    }
                }
            }
            then("remove") {
                then("players" to players()) {
                    suspendingExecutes { context ->
                        val processor: GameProcessor by context
                        val players: Collection<Player> by context
                        players.forEach(processor::removePlayer)
                    }
                }
            }
        }
    }
}