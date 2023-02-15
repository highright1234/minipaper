package io.github.highright1234.minipaperdebug

import io.github.highright1234.minipaper.game.EliminatableGameProcessor
import io.github.highright1234.minipaper.util.scoreboard.SmartScoreboard
import io.github.highright1234.minipaper.util.toPlayer
import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*


// 빌드배틀 같은건 GameProcessor 쓰고
// pvp, bed-wars 같은거는 EliminatableGameProcessor
class TestGameProcessor : EliminatableGameProcessor() {

    private val kills = gameInfo.getProperty("kills")
    private val deaths by gameInfo

    override var scoreboard: SmartScoreboard? = SmartScoreboard {
        displayName = player.displayName()
        + "===================="
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
        + "===================="
    }

    override suspend fun onCreated() {
        registerListenersOf("game.listener")
    }

    private val spawn1 = Location(Bukkit.getWorld("world"), 5.0, 0.0, 0.0)
    private val spawn2 = Location(Bukkit.getWorld("world"), -5.0, 0.0, 0.0)

    override suspend fun onStart() {
        val spawns = listOf(spawn1, spawn2).iterator()
        onlinePlayers.forEach { player ->
            player.teleport(spawns.next())
            player.inventory.setItem(0, ItemStack(Material.DIAMOND_SWORD))
            // TestListener에서 플레이어 죽는거 확인함
        }
    }

    override suspend fun onEliminated(uniqueId: UUID) {
        deaths.setAsync(uniqueId, 1)
    }

    override suspend fun onStop() {
        winner?.toPlayer()?.let {
            it.sendMessage("ㅊㅊㅊㅊㅊ")
            kills.plusAsync(it.uniqueId, 1)
        }
    }
}