package io.github.highright1234.minipaper.game.event

import io.github.highright1234.minipaper.game.GameProcessor
import kotlinx.coroutines.flow.SharedFlow
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import kotlin.reflect.KClass

interface GameEventManager {
    suspend fun <T : Event> listen(
        gameProcessor: GameProcessor,
        player: Player,
        clazz: Class<T>,
        listeningAllEvent : Boolean = false,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        filter: (T) -> Boolean = { true },
    ): Result<T>

    suspend fun <T: Event> listen(
        gameProcessor: GameProcessor,
        clazz: Class<T>,
        listeningAllEvent : Boolean = false,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        filter: (T) -> Boolean = { true },
    ): T

    fun <T : Event> listenEvents(
        gameProcessor: GameProcessor,
        clazz: Class<T>,
        listeningAllEvent : Boolean = false,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
    ): SharedFlow<T>

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