package io.github.highright1234.shotokonoko.internal.game.event

import io.github.highright1234.shotokonoko.game.GameProcessor
import io.github.highright1234.shotokonoko.game.event.GameEventManager
import io.github.highright1234.shotokonoko.game.event.ListeningAllEvent
import io.github.highright1234.shotokonoko.game.event.RegisterBeforeStart
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.bukkit.Bukkit
import org.bukkit.event.*
import org.bukkit.event.entity.PlayerLeashEntityEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.EventExecutor
import java.lang.reflect.InvocationTargetException

object GameEventManagerImpl : GameEventManager {

    private var listeners = mapOf<GameProcessor, List<Listener>>()

    //    private var listeners = LinkedListMultimap.create<GameProcessor, Listener>()
    override fun <T : Event> listen(
        gameProcessor: GameProcessor,
        clazz: Class<T>,
        listeningAllEvent: Boolean,
        priority: EventPriority,
        ignoreCancelled: Boolean,
    ): Flow<T> {
        val flow = MutableSharedFlow<T>()
        lateinit var listener: Listener
        listener = object : Listener {}
        @Suppress("UNCHECKED_CAST")
        val eventExecutor = EventExecutor { _, event ->
            if (listeningAllEvent == isGamesEvent(event, gameProcessor)) {
                HandlerList.unregisterAll(listener)
                runBlocking {
                    flow.emit(event as T)
                }
            }
        }
        registerEvent(
            clazz, listener,
            EventPriority.NORMAL,
            eventExecutor,
            gameProcessor,
            ignoreCancelled
        )
        registerListener(gameProcessor, listener)
        return flow
    }

    override fun registerListener(gameProcessor: GameProcessor, listener: Listener) {
        listener::class.java.methods
            .filter { it.isAnnotationPresent(ListeningAllEvent::class.java) }
            .filter { !it.isBridge || !it.isSynthetic }
            .forEach { method ->
                if (method.parameterCount != 1) {
                    gameProcessor.plugin.logger.severe(
                        gameProcessor.plugin.description
                            .fullName + " attempted to register an invalid EventHandler method signature \""
                                + method.toGenericString() + "\" in " + listener.javaClass
                    )
                    return@forEach
                }

                val eventClass = method.parameterTypes[0].asSubclass(Event::class.java)
                val handler = method.getAnnotation(EventHandler::class.java)
                val isListeningAllEvent = method.isAnnotationPresent(ListeningAllEvent::class.java)
                val isRegisterBeforeStart = method.isAnnotationPresent(RegisterBeforeStart::class.java)
                val run = fun Listener.(event: Event) { method.invoke(this, event) }

                val eventExecutor = eventExecutor(eventClass) { listener, event ->
                    if (isRegisterBeforeStart || gameProcessor.isStarted) {
                        if (isListeningAllEvent) {
                            listener.run(event)
                        } else {
                            if (isGamesEvent(event, gameProcessor)) {
                                listener.run(event)
                            }
                        }
                    }
                }
                registerEvent(
                    eventClass,
                    listener,
                    handler.priority,
                    eventExecutor,
                    gameProcessor,
                    handler.ignoreCancelled
                )
            }
    }

    override fun unregisterListeners(gameProcessor: GameProcessor) {
        listeners
            .getOrDefault(gameProcessor, listOf())
            .forEach(HandlerList::unregisterAll)
        listeners = listeners.minus(gameProcessor)
    }


    private fun registerEvent(
        clazz: Class<out Event>,
        listener: Listener,
        priority: EventPriority,
        eventExecutor: EventExecutor,
        gameProcessor: GameProcessor,
        ignoreCancelled: Boolean
    ) {
        Bukkit.getPluginManager().registerEvent(
            clazz,
            listener,
            priority,
            eventExecutor,
            gameProcessor.plugin,
            ignoreCancelled
        )
        listeners
            .getOrElse(gameProcessor) { listOf() }
            .let {
                listeners = listeners.plus(
                    gameProcessor to it.plus(listener)
                )
            }
    }

    private fun <T : Event> eventExecutor(
        clazz: Class<T>, listenerData: (listener: Listener, event: T) -> Unit
    ) = EventExecutor { listener, event ->
        kotlin.runCatching {
            if (!clazz.isAssignableFrom(event.javaClass)) return@EventExecutor
            @Suppress("UNCHECKED_CAST")
            listenerData(listener, event as T)
        }.exceptionOrNull()?.let {
            if (it is InvocationTargetException) throw EventException(it.cause)
        }
    }

    private fun isGamesEvent(event: Event, gameProcessor: GameProcessor): Boolean =
        when (event) {
            is PlayerEvent -> event.player in gameProcessor.players
            is PlayerLeashEntityEvent -> event.player in gameProcessor.players
            else -> true
        }
}