package io.github.highright1234.minipaper.game.event

import io.github.highright1234.minipaper.game.GameProcessor
import kotlinx.coroutines.flow.Flow
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import kotlin.reflect.KClass

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

    fun registerListeners(
        gameProcessor: GameProcessor, vararg listeners : KClass<out Listener>
    )

    fun unregisterListeners(
        gameProcessor: GameProcessor
    )

}