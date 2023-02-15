package io.github.highright1234.minipaper.event.queue

import io.github.highright1234.minipaper.game.GameInfo
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class QueueEvent(val gameInfo: GameInfo) : Event() {
    companion object {
        private val handlerList = HandlerList()
        @JvmStatic
        fun getHandlerList() = handlerList
    }
    override fun getHandlers() = handlerList
}