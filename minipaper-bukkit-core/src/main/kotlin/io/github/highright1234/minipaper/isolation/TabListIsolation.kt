@file:Suppress("UnstableApiUsage")

package io.github.highright1234.minipaper.isolation

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import io.github.highright1234.minipaper.Permissions
import io.github.highright1234.minipaper.event.processor.ProcessorPlayerJoinEvent
import io.github.highright1234.minipaper.event.processor.ProcessorPlayerQuitEvent
import io.github.highright1234.minipaper.internal.MiniPaperImpl.plugin
import io.github.highright1234.minipaper.util.processor
import io.github.monun.tap.protocol.PacketSupport
import io.github.monun.tap.protocol.PlayerInfoUpdateAction
import io.github.monun.tap.protocol.sendPacket
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.server.PluginDisableEvent

object TabListIsolation: IsolationSetting(), Listener {

    override val bypassPermission: String = Permissions.Bypass.TAB_LIST_BYPASS

    override suspend fun init() {
        plugin.server.pluginManager.registerSuspendingEvents(this, plugin)
    }

    var graph: MutableGraph<Player> = GraphBuilder.directed().allowsSelfLoops(false).build()

    fun reset(player: Player) {
        // TODO 값 어케 넣을지 생각좀
        val bypass: Boolean = true
        graph.adjacentNodes(player).forEach {
            player show it
        }
        if (!bypass) {
            Bukkit.getOnlinePlayers().forEach { other ->
                if (player.world == other.world) return
                val processor = player.processor
                val playersToHide = Bukkit.getWorlds().minus(player.world)
                    .map { it.players }.flatten()
                    .filter { processor != it.processor }

                player hideEachOther playersToHide
            }
        }
    }

    // TODO 펄미션이나 커맨드로 따로 새로 업데이트됐을떄 바뀌는거 설정

    @EventHandler
    fun PlayerJoinEvent.on() {
        if (!isEnabled) return
        val playersToIgnore = Bukkit.getOnlinePlayers().toMutableSet()
        val processor = player.processor
        val isPlaying = processor != null

        if (isPlaying) playersToIgnore -= processor!!.onlinePlayers.toSet()

        playersToIgnore -= player.world.players.toSet()

        player hideEachOther playersToIgnore

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun PlayerQuitEvent.on() {
        if (!isEnabled) return
        graph.removeNode(player)
    }

    infix fun Player.hide(other: Player) {
        if (this !in bypasses) {
            PacketSupport.playerInfoRemove(other).let(::sendPacket)
            graph.putEdge(this, other)
        }
    }

    infix fun Player.hideEachOther(iterable: Iterable<Player>) {
        iterable.forEach { other ->
            hide(other)
            other.hide(this)
        }
    }

    infix fun Player.show(other: Player) {
        if (graph.removeEdge(this, other)) {
            PacketSupport.playerInfoUpdate(PlayerInfoUpdateAction.ADD_PLAYER, other).let(::sendPacket)
        }
    }

    infix fun Player.showEachOther(iterable: Iterable<Player>) {
        iterable.forEach { other ->
            show(other)
            other.show(this)
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun PlayerTeleportEvent.on() {
        if (!isEnabled) return
        if (from.world == to.world) return

        val processor = player.processor

        val playersToHide = from.world!!.players
            .filter { processor != it.processor }

        player hideEachOther playersToHide

        player showEachOther to.world!!.players
    }

    @EventHandler(ignoreCancelled = true)
    fun ProcessorPlayerJoinEvent.on() {
        if (!isEnabled) return
        val player = player
        player showEachOther gameProcessor.onlinePlayers
        val playersToHide = Bukkit
            .getOnlinePlayers()
            .minus(gameProcessor.onlinePlayers.toSet())
            .minus(player.world.players.toSet())

        player hideEachOther playersToHide
    }

    @EventHandler(ignoreCancelled = true)
    fun ProcessorPlayerQuitEvent.on() {
        if (!isEnabled) return
        val playersToHide = gameProcessor.onlinePlayers.filter { player.world != it.world }
        player hideEachOther playersToHide
    }


    @EventHandler
    fun PluginDisableEvent.on() {
        graph.nodes().forEach {
            it showEachOther graph.adjacentNodes(it)
        }
    }
}