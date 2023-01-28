package io.github.highright1234.minipaper.event.processor

import io.github.highright1234.minipaper.game.GameProcessor
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class ProcessorEvent(val gameProcessor: GameProcessor) : Event() {
    companion object {
        private val handlerList = HandlerList()
        @JvmStatic
        fun getHandlerList() = handlerList
    }
    override fun getHandlers() = handlerList
}