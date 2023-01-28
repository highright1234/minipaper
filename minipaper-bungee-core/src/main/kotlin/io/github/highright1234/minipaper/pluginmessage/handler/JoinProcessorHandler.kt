package io.github.highright1234.minipaper.pluginmessage.handler

import com.google.common.io.ByteArrayDataInput
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*

object JoinProcessorHandler: PluginMessageHandler {
    override fun handle(receiver: ProxiedPlayer, bytes: ByteArrayDataInput) {
        val processor = bytes.readProcessor()
        receiver.connect(processor.owner)
    }
}