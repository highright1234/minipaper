package io.github.highright1234.minipaper.event.queue

import io.github.highright1234.minipaper.game.GameInfo
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable

class QueueJoinEvent(gameInfo: GameInfo, val player: Player) : QueueEvent(gameInfo), Cancellable {
    private var isCancelled = false
    override fun isCancelled(): Boolean = isCancelled

    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }
}