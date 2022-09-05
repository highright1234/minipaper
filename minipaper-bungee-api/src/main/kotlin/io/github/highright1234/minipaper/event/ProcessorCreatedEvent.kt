package io.github.highright1234.minipaper.event

import io.github.highright1234.minipaper.game.GameProcessorInfo
import net.md_5.bungee.api.plugin.Event

class ProcessorCreatedEvent(val gameProcessorInfo: GameProcessorInfo): Event()