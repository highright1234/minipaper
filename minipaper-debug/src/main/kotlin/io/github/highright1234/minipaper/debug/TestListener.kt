package io.github.highright1234.minipaper.debug

import io.github.highright1234.minipaper.game.event.GameListener
import io.github.highright1234.minipaper.game.event.ListeningAllEvent
import io.github.highright1234.minipaper.game.event.RegisterBeforeStart
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent

class TestListener : GameListener<TestGameProcessor>() {
    @EventHandler
    @RegisterBeforeStart
    @ListeningAllEvent
    suspend fun PlayerDeathEvent.on() {
        player.sendMessage("응애네")
    }
}