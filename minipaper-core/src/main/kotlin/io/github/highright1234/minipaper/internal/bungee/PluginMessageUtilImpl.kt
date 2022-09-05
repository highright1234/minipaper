package io.github.highright1234.minipaper.internal.bungee

import io.github.highright1234.minipaper.bungee.PluginMessageUtil
import kotlinx.coroutines.Deferred
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PluginMessageUtilImpl : PluginMessageUtil {
    private val onlinePlayers get() = Bukkit.getOnlinePlayers().toList()
    private fun requiresOnlinePlayers() {
        require(onlinePlayers.isNotEmpty()) { "requires least one player join the server." }
        if (onlinePlayers.isEmpty()) {
            throw IllegalStateException("no players on the server")
        }
    }
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
        requiresOnlinePlayers()
        TODO("Not yet implemented")
    }

    override fun getPlayerList(server: String): Deferred<List<String>> {
        requiresOnlinePlayers()
        TODO("Not yet implemented")
    }

    override fun forwardMessage(channel: String, message: ByteArray) {
        requiresOnlinePlayers()
        TODO("Not yet implemented")
    }

    override fun forwardMessageToPlayer(channel: String, message: ByteArray) {
        requiresOnlinePlayers()
        TODO("Not yet implemented")
    }

    override fun getServers(): Deferred<List<String>> {
        requiresOnlinePlayers()
        TODO("Not yet implemented")
    }
}