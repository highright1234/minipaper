package io.github.highright1234.minipaper.debug

import io.github.highright1234.minipaper.game.GameProcessor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack

class TestGameProcessor : GameProcessor("test") {
    init {
        listeners = listOf(
            TestListener::class
        )
    }
    private val spawn = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)
    override suspend fun onStart() {
        players.forEach { it.teleport(spawn) }
        delay(1000L)
        players.forEach {
            it.inventory.setItem(0, ItemStack(Material.DIAMOND_SWORD))
        }
        val event = listen(PlayerDeathEvent::class.java).filter {
            it.player.inventory.itemInMainHand.type == Material.DIAMOND_SWORD
        }.first()
        delay(1000L)
        event.player.giveExp(1000)
    }
}