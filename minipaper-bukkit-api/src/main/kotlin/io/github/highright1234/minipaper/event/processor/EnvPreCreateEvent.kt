package io.github.highright1234.minipaper.event.processor

import io.github.highright1234.minipaper.game.GameProcessor
import org.bukkit.World

class EnvPreCreateEvent(
    val worldName: String,
    val origin: String,
    gameProcessor: GameProcessor
): ProcessorEvent(gameProcessor) {
    var world: World? = null
}