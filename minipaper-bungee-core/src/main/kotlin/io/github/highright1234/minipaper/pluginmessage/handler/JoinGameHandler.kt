package io.github.highright1234.minipaper.pluginmessage.handler

import com.google.common.io.ByteArrayDataInput
import io.github.highright1234.minipaper.PluginChannels
import io.github.highright1234.shotokonoko.listener.listen
import io.github.highright1234.shotokonoko.pluginmessage.send
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ServerSwitchEvent

object JoinGameHandler : PluginMessageHandler {
    override suspend fun handle(receiver: ProxiedPlayer, bytes: ByteArrayDataInput) {
        val game = bytes.readGame()
        receiver.listen<ServerSwitchEvent>().onSuccess { _ ->
            receiver.server.send(channel) {
                writeUTF(PluginChannels.SubChannels.JOIN_MINI_GAME)
                writeUTF(game.name)
            }
        }
    }
}