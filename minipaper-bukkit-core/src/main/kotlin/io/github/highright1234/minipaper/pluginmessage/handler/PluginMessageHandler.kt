package io.github.highright1234.minipaper.pluginmessage.handler

import com.google.common.io.ByteArrayDataInput
import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.internal.MiniPaperImpl
import org.bukkit.entity.Player
import java.util.UUID


fun ByteArrayDataInput.readGame(): GameInfo {
    return readUTF().let { MiniPaperImpl.gameManager.games.getOrElse(it) { error("Not found Mini-game") } }
}

fun ByteArrayDataInput.readProcessor(): GameProcessor {
    return readUTF()
        .let(UUID::fromString)
        .let { uniqueId ->
            MiniPaperImpl.gameManager.gameProcessors.values.flatten().first { it.uniqueId == uniqueId }
        }
}

interface PluginMessageHandler {
    fun handle(sender : Player, bytes : ByteArrayDataInput)
}