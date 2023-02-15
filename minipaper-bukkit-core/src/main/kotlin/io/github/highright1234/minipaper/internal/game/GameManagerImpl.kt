package io.github.highright1234.minipaper.internal.game

import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.google.common.collect.MapMaker
import io.github.highright1234.minipaper.PriorityBy
import io.github.highright1234.minipaper.config.MiniPaperConfig
import io.github.highright1234.minipaper.event.processor.ProcessorCreatedEvent
import io.github.highright1234.minipaper.event.processor.ProcessorDeletedEvent
import io.github.highright1234.minipaper.event.processor.ProcessorPlayerQuitEvent
import io.github.highright1234.minipaper.game.EliminatableGameProcessor
import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameManager
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.internal.MiniPaperImpl.plugin
import io.github.highright1234.shotokonoko.listener.events
import io.github.highright1234.shotokonoko.listener.listen
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.reflect.full.memberFunctions

object GameManagerImpl: GameManager {

    internal fun init() {
        plugin.launch {
            events<ProcessorDeletedEvent>().collect { event ->
                removeData(event.gameProcessor)
                val game = event.gameProcessor.gameInfo
                val defaultNumber = game.processorSetting.defaultNumber
                val processorNumber = game.processors.size
                if (processorNumber < defaultNumber) {
                    createProcessor(game)
                }
            }
        }
    }

    override var priorityBy: PriorityBy = MiniPaperConfig.priorityBy

    private val gameProcessorClasses = MapMaker().weakKeys().makeMap<GameInfo, Class<out GameProcessor>>()
    private val processors: MutableMap<GameInfo, MutableList<GameProcessor>> = MapMaker().weakKeys().makeMap()

    override val games: Map<String, GameInfo>
        get() = gameProcessors
            .values.flatten()
            .map { it.gameInfo }.toSet()
            .associateBy { it.name }
    override val gameProcessors: Map<GameInfo, Collection<GameProcessor>> get() = processors.toMap()

    override suspend fun register(gameInfo: GameInfo, gameProcessorClass: Class<out GameProcessor>) {
        repeat(gameInfo.processorSetting.defaultNumber) {
            createProcessor(gameInfo)
        }
    }

    override suspend fun createProcessor(gameInfo: GameInfo) {
        withContext(plugin.minecraftDispatcher) {
            require(gameInfo in gameProcessorClasses) {
                "games must be registered before create the processor"
            }
            val gameProcessorClass = gameProcessorClasses[gameInfo]!!
            val processor = gameProcessorClass.getConstructor().newInstance()
            GameProcessor::class.memberFunctions
                .find { it.name == "init" }
                ?.call(processor, gameInfo)
            kotlin.runCatching {
                processor.onCreated()
            }.exceptionOrNull()?.printStackTrace()
            if (processor is EliminatableGameProcessor) {
                launch {
                    val job = plugin.launch {
                        events<ProcessorPlayerQuitEvent>()
                            .filter { it.gameProcessor == processor }
                            .collect { event ->
                                processor.eliminatePlayer(event.player)
                            }
                    }
                    listen<ProcessorDeletedEvent> { it.gameProcessor == processor }
                    job.cancel()
                }
            }
            addData(processor)
            ProcessorCreatedEvent(processor).callEvent()
        }
    }

    private val mutex = Mutex()

    private suspend fun addData(processor: GameProcessor) {
        mutex.withLock {
            val gameInfo = processor.gameInfo
            processors.getOrPut(gameInfo) { mutableListOf() }.add(processor)
        }
    }

    private suspend fun removeData(processor: GameProcessor) {
        mutex.withLock {
            val gameInfo = processor.gameInfo
            processors[gameInfo]?.remove(processor)
            if (processors[gameInfo]?.isEmpty() == true) {
                processors -= gameInfo
            }
        }
    }
}