package io.github.highright1234.shotokonoko.event

import io.github.highright1234.shotokonoko.game.GameProcessorInfo
import net.md_5.bungee.api.plugin.Event

class ProcessorDeletedEvent(val gameProcessorInfo: GameProcessorInfo) : Event()