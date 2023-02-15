package io.github.highright1234.minipaper

import io.github.highright1234.minipaper.util.processor
import io.github.highright1234.shotokonoko.papi.papi
import kotlinx.coroutines.runBlocking

object Papi {
    fun register() {
        papi {
            expansion("minipaper") {
                then("playing_game") {
                    executes {
                        player?.processor?.gameInfo?.name
                    }
                }
                argument("game") {
                    then("player_count") {
                        executes {
                            val gameName by arguments
                            MiniPaper.gameManager.games[gameName]!!
                                .processors
                                .sumOf { it.onlinePlayers.size }
                                .toString() // TODO bungee
                        }
                    }
                    then("datastore") {
                        argument("property") {
                            argument("field") {
                                executes {
                                    val gameName by arguments
                                    val property by arguments
                                    val field by arguments
                                    val game = MiniPaper.gameManager.games[gameName]!!
                                    val datastore = game.getProperty(property)
                                    runBlocking { datastore.get(field, String::class.java) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}