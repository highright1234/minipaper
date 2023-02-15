package io.github.highright1234.minipaper.event.queue

import io.github.highright1234.minipaper.game.GameInfo
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable

class QueueQuitEvent(gameInfo: GameInfo, val player: Player) : QueueEvent(gameInfo)