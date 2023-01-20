package io.github.highright1234.minipaper.event

import io.github.highright1234.minipaper.game.GameProcessor
import org.bukkit.entity.Player

class ProcessorPlayerJoinEvent(gameProcessor: GameProcessor, val player: Player) : ProcessorEvent(gameProcessor)