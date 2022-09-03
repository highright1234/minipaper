package io.github.highright1234.shotokonoko.kommand

import io.github.monun.kommand.PluginKommand

object ShotokonokoKommand {
    fun register(pk : PluginKommand) {
        pk.register("shotokonoko", "stknk", "stk") {
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