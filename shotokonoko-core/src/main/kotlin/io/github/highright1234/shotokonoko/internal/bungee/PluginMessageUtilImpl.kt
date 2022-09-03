package io.github.highright1234.shotokonoko.internal.bungee

import io.github.highright1234.shotokonoko.bungee.PluginMessageUtil
import kotlinx.coroutines.Deferred
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

object PluginMessageUtilImpl : PluginMessageUtil {
    override fun sendPlayer(player: Player, server: String) {
        TODO("Not yet implemented")
    }

    override fun sendPlayer(name: String, server: String) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(name: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(name: String, message: Component) {
        TODO("Not yet implemented")
    }

    override fun getPlayerCount(server: String): Deferred<Int> {
        TODO("Not yet implemented")
    }

    override fun getPlayerList(server: String): Deferred<List<String>> {
        TODO("Not yet implemented")
    }

    override fun forwardMessage(channel: String, message: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun forwardMessageToPlayer(channel: String, message: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun getServers(): Deferred<List<String>> {
        TODO("Not yet implemented")
    }
}