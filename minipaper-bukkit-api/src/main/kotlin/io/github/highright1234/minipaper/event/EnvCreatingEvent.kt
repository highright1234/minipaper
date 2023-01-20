package io.github.highright1234.minipaper.event

import io.github.highright1234.minipaper.game.GameProcessor
import org.bukkit.World

class EnvCreatingEvent(val worldName: String, val origin: World, gameProcessor: GameProcessor): ProcessorEvent(gameProcessor) {
    var world: World? = null
}