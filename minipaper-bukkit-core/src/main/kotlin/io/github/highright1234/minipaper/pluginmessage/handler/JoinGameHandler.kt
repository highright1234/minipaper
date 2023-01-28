package io.github.highright1234.minipaper.pluginmessage.handler

import com.google.common.io.ByteArrayDataInput
import io.github.highright1234.minipaper.util.joinGame
import org.bukkit.entity.Player

object JoinGameHandler : PluginMessageHandler {
    override fun handle(sender : Player, bytes: ByteArrayDataInput) {
        val game = bytes.readGame()
        sender.joinGame(game)
    }
}