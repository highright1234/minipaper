package io.github.highright1234.minipaper.pluginmessage

import com.google.common.io.ByteStreams
import io.github.highright1234.minipaper.PluginChannels
import io.github.highright1234.minipaper.pluginmessage.handler.JoinGameHandler
import io.github.highright1234.minipaper.pluginmessage.handler.JoinProcessorHandler
import io.github.highright1234.minipaper.pluginmessage.handler.PluginMessageHandler
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

object BungeePluginMessageListener : PluginMessageListener {
    private val processorOf : Map<String, PluginMessageHandler> = mapOf(
        PluginChannels.SubChannels.JOIN_MINI_GAME to JoinGameHandler,
        PluginChannels.SubChannels.JOIN_PROCESSOR to JoinProcessorHandler,
    )

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != PluginChannels.CHANNEL) return
        @Suppress("UnstableApiUsage")
        val input = ByteStreams.newDataInput(message)
        val subChannel = input.readUTF()
        processorOf[subChannel]?.handle(player, input) ?: run {
            error("Couldn't find sub channel, sub-channel: $subChannel")
        }
    }
}