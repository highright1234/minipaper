package io.github.highright1234.minipaper.pluginmessage.handler

import com.google.common.io.ByteArrayDataInput
import io.github.highright1234.minipaper.PluginChannels
import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameProcessorInfo
import io.github.highright1234.minipaper.internal.MiniPaperImpl
import io.github.highright1234.shotokonoko.pluginmessage.MessageChannel
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.UUID


fun ByteArrayDataInput.readGame(): GameInfo {
    return readUTF().let { MiniPaperImpl.gameManager.games.getOrElse(it) { error("Not found Mini-game") } }
}

fun ByteArrayDataInput.readProcessor(): GameProcessorInfo {
    return readUTF()
        .let(UUID::fromString)
        .let { uniqueId ->
            MiniPaperImpl.gameManager.gameProcessors.values.flatten().first { it.uniqueId == uniqueId }
        }
}

val channel = MessageChannel(PluginChannels.CHANNEL)

interface PluginMessageHandler {
    suspend fun handle(receiver : ProxiedPlayer, bytes : ByteArrayDataInput)
}