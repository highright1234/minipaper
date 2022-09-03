package io.github.highright1234.shotokonoko.game.event

import io.github.highright1234.shotokonoko.game.GameProcessor
import kotlinx.coroutines.flow.Flow
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

interface GameEventManager {

    fun <T: Event> listen(
        gameProcessor: GameProcessor,
        clazz: Class<T>,
        listeningAllEvent : Boolean = false,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
    ) : Flow<T>

    fun registerListener(
        gameProcessor: GameProcessor, listener: Listener
    )

    fun unregisterListeners(
        gameProcessor: GameProcessor
    )

}