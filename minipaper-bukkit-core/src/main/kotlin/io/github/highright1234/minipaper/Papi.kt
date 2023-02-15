package io.github.highright1234.minipaper

import io.github.highright1234.shotokonoko.papi.papi
import kotlinx.coroutines.runBlocking

object Papi {
    fun register() {
        papi {
            expansion("minipaper") {
                argument("game") {
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