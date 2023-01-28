package io.github.highright1234.minipaper.kommand

import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.MinipaperPlugin
import io.github.highright1234.minipaper.Permissions
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.kommand.processor.Team
import io.github.highright1234.minipaper.kommand.processor.Var
import io.github.highright1234.shotokonoko.monun.suspendingExecutes
import io.github.monun.kommand.KommandContext
import io.github.monun.kommand.PluginKommand
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import net.kyori.adventure.text.Component.text
import org.bukkit.permissions.Permission
import io.github.highright1234.minipaper.kommand.processor.Player as PlayerNode

object MiniPaperKommand {

    private fun KommandNode.processor() = dynamic { _: KommandContext, input: String ->
        MiniPaper.gameManager.gameProcessors.values.flatten()
            .find { it.toString() == input }
    }.apply {
        suggests { _ ->
            MiniPaper.gameManager.gameProcessors.values.flatten()
                .map { it.uniqueId.toString() }
                .let(::suggest)
        }
    }

    fun register(pk : PluginKommand) {
        pk.register("minipaper", "mp") {
            permission = Permission(Permissions.COMMAND)
            then("info") {
                suspendingExecutes {
                    val message = """
                      |is-bungee: ${MinipaperPlugin.plugin.isBungee}
                      |is-debug: ${MiniPaper.isDebug}
                      |running-games: ${MiniPaper.gameManager.games.values.joinToString()}
                      |running-processor: ${MiniPaper.gameManager.gameProcessors.values.flatten().size}
                    """
                    player.sendMessage(text(message))
                }
            }

            then("processor" to processor()) {
                then("start") {
                    suspendingExecutes { context ->
                        val processor: GameProcessor by context
                        processor.start()
                    }
                }
                then("stop") {
                    suspendingExecutes { context ->
                        val processor: GameProcessor by context
                        processor.stop()
                    }
                }

                Team.init(this)

                PlayerNode.init(this)

                Var.init(this)

            }
        }
    }

}