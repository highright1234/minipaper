package io.github.highright1234.minipaperdebug

import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.util.scoreboard.SmartScoreboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack

class TestGameProcessor : GameProcessor(GameInfo("test", MinipaperDebugPlugin.plugin)) {

    override suspend fun onCreated() {
        registerListenersOf("game.listener")
        scoreboard = SmartScoreboard {
            displayName = player.displayName()
            if (player.name != "HighRight") {
                + player.displayName().append(Component.text(" is smart"))
                + "You are handsome"
                + "Why are you so perfect~"
                + "Handsome~ Cute~ Smart~ Perfect~"
                + "You are the most perfect masterpiece in the world"
            } else {
                + "I think you are ugly"
                + "Bruh"
                + "It ain't a joke"
                + "LOL"
                + "You idiot :)"
            }
        }
    }

    private val spawn = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)

    private var winner: Player? = null

    override suspend fun onStart() {
        onlinePlayers.forEach { player ->
            player.teleport(spawn)
            delay(1000L)
            player.inventory.setItem(0, ItemStack(Material.DIAMOND_SWORD))
            delay(1000L)
            listen(PlayerDeathEvent::class.java).first {
                it.player == player
            }
            player.sendMessage("축하 드려유 히히")
            winner = player.killer
            stop()
        }
    }

    override suspend fun onStop() {
        winner?.sendMessage("ㅊㅊㅊㅊㅊ")
    }
}