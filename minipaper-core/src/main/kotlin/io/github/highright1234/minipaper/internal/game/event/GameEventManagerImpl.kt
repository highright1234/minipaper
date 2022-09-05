package io.github.highright1234.minipaper.internal.game.event

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.game.event.GameEventManager
import io.github.highright1234.minipaper.game.event.ListeningAllEvent
import io.github.highright1234.minipaper.game.event.RegisterBeforeStart
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.bukkit.Bukkit
import org.bukkit.event.*
import org.bukkit.event.entity.PlayerLeashEntityEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.EventExecutor
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaMethod

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
        listener::class.functions
            .filter { it.hasAnnotation<ListeningAllEvent>() }
            .forEach { function ->
                if (function.parameters.size != 1 == (function.isSuspend && function.parameters.size != 2)) {
                    gameProcessor.plugin.logger.severe(
                        gameProcessor.plugin.description
                            .fullName + " attempted to register an invalid EventHandler method signature \""
                                + function.name + "\" in " + listener.javaClass
                    )
                    return@forEach
                }

                val eventClass = function.javaMethod!!.parameters[0].javaClass.asSubclass(Event::class.java)
                val handler = function.findAnnotation<EventHandler>()!!
                val isListeningAllEvent = function.hasAnnotation<ListeningAllEvent>()
                val isRegisterBeforeStart = function.hasAnnotation<RegisterBeforeStart>()
                val run = fun Listener.(event: Event) { runBlocking { function.callSuspend(event) } }

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