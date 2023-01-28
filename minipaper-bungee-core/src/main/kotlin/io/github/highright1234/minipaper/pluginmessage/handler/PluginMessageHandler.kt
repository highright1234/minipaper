package io.github.highright1234.minipaper.pluginmessage.handler

import com.google.common.io.ByteArrayDataInput
import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameProcessorInfo
import io.github.highright1234.minipaper.internal.MiniPaperImpl
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

interface PluginMessageHandler {
    fun handle(receiver : ProxiedPlayer, bytes : ByteArrayDataInput)
}