package io.github.highright1234.minipaper.event.queue

import io.github.highright1234.minipaper.game.GameInfo
import org.bukkit.entity.Player

class QueuePollEvent(gameInfo: GameInfo, val n : Int, var result: List<Player>) : QueueEvent(gameInfo)