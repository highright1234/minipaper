package io.github.highright1234.minipaper.bungee

import kotlinx.coroutines.Deferred
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

interface PluginMessageUtil {

    fun sendPlayer(player: Player, server : String)
    fun sendPlayer(name : String, server: String)
    // if name is ALL, bungee will broadcast it
    fun sendMessage(name: String, message: String)
    fun sendMessage(name : String, message: Component)

    @Suppress("DeferredIsResult")
    fun getPlayerCount(server: String = "ALL") : Deferred<Int>
    @Suppress("DeferredIsResult")
    fun getPlayerList(server : String = "ALL") : Deferred<List<String>>

    fun forwardMessage(channel: String, message: ByteArray)
    fun forwardMessageToPlayer(channel: String, message: ByteArray)

    @Suppress("DeferredIsResult")
    fun getServers() : Deferred<List<String>>
}