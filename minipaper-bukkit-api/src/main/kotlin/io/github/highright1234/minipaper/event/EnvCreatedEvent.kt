package io.github.highright1234.minipaper.event

import io.github.highright1234.minipaper.game.GameProcessor
import org.bukkit.World

class EnvCreatedEvent(val world: World, val origin: World, gameProcessor: GameProcessor): ProcessorEvent(gameProcessor)