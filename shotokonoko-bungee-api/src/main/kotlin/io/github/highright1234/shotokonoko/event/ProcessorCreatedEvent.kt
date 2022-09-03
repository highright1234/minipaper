package io.github.highright1234.shotokonoko.event

import io.github.highright1234.shotokonoko.game.GameProcessorInfo
import net.md_5.bungee.api.plugin.Event

class ProcessorCreatedEvent(val gameProcessorInfo: GameProcessorInfo): Event()