package io.github.highright1234.minipaper.isolation

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import io.github.highright1234.minipaper.Permissions
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.internal.MiniPaperImpl
import io.github.highright1234.minipaper.util.processor
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent

object ChatIsolation: IsolationSetting(), Listener {

    override val bypassPermission: String = Permissions.Bypass.CHAT_BYPASS

    override suspend fun init() {
        MiniPaperImpl.plugin.server.pluginManager.registerSuspendingEvents(this, MiniPaperImpl.plugin)
    }
    // TODO 관리자 기능

    val playingPlayers: Iterable<Player> get() =
    MiniPaperImpl.gameManager
        .gameProcessors.values.flatten()
        .map { it.onlinePlayers }.flatten()

    val nonPlayingPlayers: Iterable<Player> get() =
        Bukkit.getOnlinePlayers().minus(playingPlayers.toSet())


    @EventHandler(priority = EventPriority.LOW)
    fun AsyncChatEvent.on() {
        if (!isEnabled) return
        val viewers = player.processor?.onlinePlayers ?: run {
            nonPlayingPlayers
        }
        viewers.sendMessage(message())
        message(Component.empty())
    }

    fun Iterable<Audience>.sendMessage(component: Component) {
        (this + bypasses + Bukkit.getServer()).toSet().forEach { it.sendMessage(component) }
    }

    fun GameProcessor.sendMessage(component: Component) {
        onlinePlayers.sendMessage(component)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun PlayerDeathEvent.onDeath() {
        if (!isEnabled) return
        deathMessage()?.let { message ->
            entity.processor?.sendMessage(message) ?: run {
                nonPlayingPlayers.sendMessage(message)
            }
            deathMessage(null)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun PlayerQuitEvent.onPlayerLeave() {
        if (!isEnabled) return
        quitMessage()?.let { message ->
            player.processor?.sendMessage(message) ?: run {
                nonPlayingPlayers.sendMessage(message)
            }
            quitMessage(null)
        }
    }
}