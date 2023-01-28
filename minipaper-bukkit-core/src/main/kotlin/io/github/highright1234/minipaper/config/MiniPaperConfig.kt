package io.github.highright1234.minipaper.config

import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.PriorityBy
import io.github.highright1234.shotokonoko.config.ConfigFrame
import io.github.monun.tap.config.Config

object MiniPaperConfig: ConfigFrame() {

    @Config
    var priorityBy: PriorityBy = PriorityBy.FEW_PLAYERS
        set(value) {
            field = value
            MiniPaper.gameManager.priorityBy = value
        }

    @Config
    var isDebug: Boolean = false

    @Config
    var slimeLoader: String = "mongodb"

    object Redis {
        //  host: localhost
        //  port: 6379
        //  password:
        //  ssl: true
        @Config
        var host: String = "root"
        @Config
        var password: String = "root"
        @Config
        var database: String = "admin"
        @Config
        var port: Int = 27017
        @Config
        var ssl: Boolean = true
    }
}