package io.github.highright1234.minipaper.pluginmessage

import com.google.common.io.ByteStreams
import io.github.highright1234.minipaper.PluginChannels
import io.github.highright1234.minipaper.pluginmessage.handler.JoinGameHandler
import io.github.highright1234.minipaper.pluginmessage.handler.JoinProcessorHandler
import io.github.highright1234.minipaper.pluginmessage.handler.PluginMessageHandler
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

object PluginMessageListener : Listener {
    private val processorOf : Map<String, PluginMessageHandler> = mapOf(
        PluginChannels.SubChannels.JOIN_MINI_GAME to JoinGameHandler,
        PluginChannels.SubChannels.JOIN_PROCESSOR to JoinProcessorHandler,
    )
    @EventHandler
    suspend fun PluginMessageEvent.on() {
        if (receiver !is ProxiedPlayer || tag != PluginChannels.CHANNEL) return
        @Suppress("UnstableApiUsage")
        val input = ByteStreams.newDataInput(data)
        val subChannel = input.readUTF()
        processorOf[subChannel]?.handle(receiver as ProxiedPlayer, input) ?: run {
            error("Couldn't find sub channel, sub-channel: $subChannel")
        }
    }
}