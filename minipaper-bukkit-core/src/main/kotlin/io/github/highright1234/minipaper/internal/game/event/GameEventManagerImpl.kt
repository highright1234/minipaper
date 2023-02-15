package io.github.highright1234.minipaper.internal.game.event

import io.github.highright1234.minipaper.event.processor.ProcessorEvent
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.game.event.GameEventManager
import io.github.highright1234.minipaper.game.event.GameListener
import io.github.highright1234.minipaper.game.event.ListeningAllEvent
import io.github.highright1234.minipaper.game.event.RegisterBeforeStart
import io.github.highright1234.shotokonoko.listener.exception.PlayerQuitException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.block.BlockEvent
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.entity.PlayerLeashEntityEvent
import org.bukkit.event.hanging.HangingEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.vehicle.VehicleEvent
import org.bukkit.event.weather.WeatherEvent
import org.bukkit.event.world.WorldEvent
import org.bukkit.plugin.EventExecutor
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object GameEventManagerImpl : GameEventManager {

    private val mutex = Mutex()
    private var listeners = mapOf<GameProcessor, List<Listener>>()
    override suspend fun <T : Event> listen(
        gameProcessor: GameProcessor,
        player: Player,
        clazz: Class<T>,
        listeningAllEvent: Boolean,
        priority: EventPriority,
        ignoreCancelled: Boolean,
        filter: (T) -> Boolean
    ): Result<T> {
        val exitListener = object: Listener {  }
        val listener = object: Listener {  }
        val completableDeferred = CompletableDeferred<Result<T>>()
        registerEvent(clazz, listener, priority, eventExecutor(clazz, gameProcessor, listeningAllEvent) { _, event ->
            if (filter(event)) {
                completableDeferred.complete(Result.success(event))
                HandlerList.unregisterAll(listener)
                HandlerList.unregisterAll(exitListener)
            }
        }, gameProcessor, ignoreCancelled)
        val eventExecutor = EventExecutor { _, event ->
            event as PlayerQuitEvent
            if (player != event.player) return@EventExecutor
            completableDeferred.complete(Result.failure(PlayerQuitException()))
            HandlerList.unregisterAll(listener)
            HandlerList.unregisterAll(exitListener)
        }
        registerEvent(
            PlayerQuitEvent::class.java,
            exitListener,
            EventPriority.NORMAL,
            eventExecutor,
            gameProcessor, ignoreCancelled
        )

        return completableDeferred.await()
    }

    override suspend fun <T : Event> listen(
        gameProcessor: GameProcessor,
        clazz: Class<T>,
        listeningAllEvent: Boolean,
        priority: EventPriority,
        ignoreCancelled: Boolean,
        filter: (T) -> Boolean
    ): T {

        val listener = object: Listener {  }
        val completableDeferred = CompletableDeferred<T>()
        val eventExecutor = eventExecutor(clazz, gameProcessor, listeningAllEvent) { _, event ->
            if (filter(event)) {
                completableDeferred.complete(event)
                HandlerList.unregisterAll(listener)
            }
        }
        registerEvent(
            clazz,
            listener,
            priority,
            eventExecutor,
            gameProcessor,
            ignoreCancelled
        )
        return completableDeferred.await()
    }

    override fun <T : Event> listenEvents(
        gameProcessor: GameProcessor,
        clazz: Class<T>,
        listeningAllEvent: Boolean,
        priority: EventPriority,
        ignoreCancelled: Boolean,
    ): SharedFlow<T> {
        val flow = MutableSharedFlow<T>()
        lateinit var listener: Listener
        listener = object : Listener {}
        val eventExecutor = eventExecutor(clazz, gameProcessor, listeningAllEvent) { _, event ->
            val dispatcher =
                if (Bukkit.isPrimaryThread()) gameProcessor.minecraftDispatcher
                else gameProcessor.asyncDispatcher
            gameProcessor.scope.launch(dispatcher) {
                flow.emit(event)
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

    override fun registerListeners(gameProcessor: GameProcessor, vararg listeners: KClass<out Listener>) {
        listeners.forEach { clazz ->
            val objectOfClazz = clazz.objectInstance
            if (objectOfClazz != null) {
                registerListener(gameProcessor, objectOfClazz)
                return@forEach
            }
            val instance = clazz.createInstance()
            if (clazz.isSubclassOf(GameListener::class)) {
                clazz
                    .declaredMemberProperties
                    .filterIsInstance<KMutableProperty<GameProcessor>>()
                    .find { it.name == "_gameProcessor" }!!
                    .apply { isAccessible = true }
                    .setter.call(instance, gameProcessor)
            }
            registerListener(gameProcessor, instance)
        }
    }

    override fun registerListener(gameProcessor: GameProcessor, listener: Listener) {
        listener::class.functions
            .filter { it.hasAnnotation<EventHandler>() }
            .forEach { function ->
                if (function.parameters.size != 1 == (function.isSuspend && function.parameters.size != 2)) {
                    gameProcessor.plugin.logger.severe(
                        gameProcessor.plugin.description
                            .fullName + " attempted to register an invalid EventHandler method signature \""
                                + function.name + "\" in " + listener.javaClass
                    )
                    return@forEach
                }

                // 이상하게 첫번째는 자기껄로 되어있음(정적 함수나 오브젝트 비스무리한것 때문으로 추정)
                val eventClass = function.parameters[1].type.jvmErasure.java.asSubclass(Event::class.java)
                val handler = function.findAnnotation<EventHandler>()!!
                val isListeningAllEvent = function.hasAnnotation<ListeningAllEvent>()
                val isRegisterBeforeStart = function.hasAnnotation<RegisterBeforeStart>()
                val run = fun (event: Event) { runBlocking { function.callSuspend(event) } }

                val eventExecutor = eventExecutor(
                    eventClass, gameProcessor, isListeningAllEvent
                ) { _, event ->
                    if (isRegisterBeforeStart || gameProcessor.isStarted) {
                        run(event)
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
                if (it.contains(listener)) return
                listeners = listeners.plus(
                    gameProcessor to it.plus(listener)
                )
            }
    }

    private fun <T : Event> eventExecutor(
        clazz: Class<T>, gameProcessor: GameProcessor, isListeningAllEvent: Boolean, listenerData: (listener: Listener, event: T) -> Unit
    ) = EventExecutor { listener, event ->
        kotlin.runCatching {
            if (!clazz.isAssignableFrom(event.javaClass)) return@EventExecutor
            if (isListeningAllEvent || isGameEvent(event, gameProcessor)) {
                @Suppress("UNCHECKED_CAST")
                listenerData(listener, event as T)
            }
        }.exceptionOrNull()?.let {
            if (it is InvocationTargetException) throw EventException(it.cause)
        }
    }

    private fun isGameEvent(event: Event, gameProcessor: GameProcessor): Boolean {
        val players = gameProcessor.onlinePlayers
        val worlds = gameProcessor.worlds
        return when (event) {
            is PlayerEvent -> event.player in players
            is EntityEvent -> {
                if (event.entity is Player) {
                    event.entity in players
                } else {
                    event.entity.world in worlds
                }
            }
            is WorldEvent -> event.world in worlds
            is WeatherEvent -> event.world in worlds
            is BlockEvent -> event.block.world in worlds
            is PlayerLeashEntityEvent -> event.player in players
            is VehicleEvent -> {
                event.vehicle.passengers
                    .filterIsInstance<Player>()
                    .find { it in players }
                    ?.let { return true }
                event.vehicle.world in worlds
            }
            is HangingEvent -> event.entity.world in worlds
            is ProcessorEvent -> event.gameProcessor == gameProcessor
            else -> true
        }
    }
}