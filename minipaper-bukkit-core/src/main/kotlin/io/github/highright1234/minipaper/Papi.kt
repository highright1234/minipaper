package io.github.highright1234.minipaper

import io.github.highright1234.shotokonoko.papi.papi

object Papi {
    fun register() {
        papi {
            expansion("minipaper") {
                then("game") {
                    then("field") {
                        executes {
                            val game by arguments
                            val field by arguments

                            TODO()
                        }
                    }
                }
            }
        }
    }
}