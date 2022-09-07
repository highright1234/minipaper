package io.github.highright1234.minipaper.debug

import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameProcessor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack

class TestGameProcessor : GameProcessor(GameInfo("test", MiniPaperDebug.plugin)) {
    init {
        listeners = listOf(
            TestListener::class
        )
    }
    private val spawn = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)
    override suspend fun onStart() {
        onlinePlayers.forEach { player ->
            scope.launch {
                player.teleport(spawn)
                delay(1000L)
                player.inventory.setItem(0, ItemStack(Material.DIAMOND_SWORD))
                delay(1000L)
                listen(PlayerDeathEvent::class.java).first {
                    it.player == player
                }
                player.sendMessage("축하 드려유")
                player.killer?.sendMessage("올ㅋ")
            }
        }
    }
}