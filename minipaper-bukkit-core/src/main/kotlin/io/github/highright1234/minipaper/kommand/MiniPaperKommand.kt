package io.github.highright1234.minipaper.kommand

import io.github.monun.kommand.PluginKommand

object MiniPaperKommand {
    fun register(pk : PluginKommand) {
        pk.register("MiniPaper", "mp") {
            then("game-name" to string()) {
                then("info") {
                    TODO()
                }
                then("player") {
                    then("add") {
                        TODO()
                    }
                    then("remove") {
                        TODO()
                    }
                }
            }
        }
    }
}