package io.github.highright1234.minipaper.event.processor

import io.github.highright1234.minipaper.game.GameProcessor
import java.util.*

class PlayerEliminatedEvent(gameProcessor: GameProcessor, val player: UUID): ProcessorEvent(gameProcessor)