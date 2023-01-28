package io.github.highright1234.minipaper.event.processor

import io.github.highright1234.minipaper.game.GameProcessor
import org.bukkit.World

class EnvPostCreateEvent(
    val world: World,
    val origin: String,
    gameProcessor: GameProcessor
): ProcessorEvent(gameProcessor)