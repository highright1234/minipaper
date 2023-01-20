package io.github.highright1234.minipaper.kommand

import io.github.highright1234.minipaper.MiniPaper
import io.github.monun.kommand.PluginKommand

object MiniPaperKommand {



    fun register(pk : PluginKommand) {
        pk.register("minipaper", "mp") {
            then("info") {
                executes {

                    TODO()
                }
            }
            then("team") {
                requires { MiniPaper.runningGameProcessor?.teamCount != 0 }

            }
            then("player") {
                then("add") {
                    then("players" to players()) {
                        executes {

                            TODO()
                        }
                    }
                }
                then("remove") {
                    then("players" to players()) {
                        executes {
                            TODO()
                        }
                    }
                }
            }
            then("state") {
                then("start") {
                    executes {
                        TODO()
                    }
                }
                then("stop") {
                    executes {
                        TODO()
                    }
                }
                then("var") {
                    then("") {

                    }
                }
            }
        }
    }
}