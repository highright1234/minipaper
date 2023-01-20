package io.github.highright1234.minipaper.pluginmessage.handler

import com.google.common.io.ByteArrayDataInput
import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.internal.MiniPaperImpl
import net.md_5.bungee.api.connection.ProxiedPlayer


fun ByteArrayDataInput.readGame(): GameInfo {
    return readUTF().let { MiniPaperImpl.gameManager.games.getOrElse(it) { error("Not found Mini-game") } }
}

interface PluginMessageHandler {
    fun handle(receiver : ProxiedPlayer, bytes : ByteArrayDataInput)
}