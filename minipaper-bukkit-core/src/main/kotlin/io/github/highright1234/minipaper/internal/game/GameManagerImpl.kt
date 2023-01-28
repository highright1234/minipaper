package io.github.highright1234.minipaper.internal.game

import com.github.shynixn.mccoroutine.bukkit.launch
import com.google.common.collect.MapMaker
import io.github.highright1234.minipaper.PriorityBy
import io.github.highright1234.minipaper.config.MiniPaperConfig
import io.github.highright1234.minipaper.event.processor.ProcessorCreatedEvent
import io.github.highright1234.minipaper.event.processor.ProcessorDeletedEvent
import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameManager
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.internal.MiniPaperImpl
import io.github.highright1234.shotokonoko.listener.listen
import org.bukkit.plugin.java.JavaPlugin

object GameManagerImpl: GameManager {

    internal fun init() {
        MiniPaperImpl.plugin.launch {
            val event = listen<ProcessorDeletedEvent>()
            removeData(event.gameProcessor)
        }
    }

    override var priorityBy: PriorityBy = MiniPaperConfig.priorityBy

    override val games: Map<String, GameInfo>
        get() = gameProcessors
            .values.flatten()
            .map { it.gameInfo }.toSet()
            .associateBy { it.name }
    override val gameProcessors: Map<GameInfo, Collection<GameProcessor>> get() = processors.toMap()

    private val processors: MutableMap<GameInfo, MutableList<GameProcessor>> = MapMaker().weakKeys().makeMap()
    fun processorsOf(gameInfoImpl: GameInfo) : Collection<GameProcessor> = processors.getOrElse(gameInfoImpl) { mutableListOf() }

    private fun removeData(processor: GameProcessor) {
        val gameInfo = processor.gameInfo
        processors[gameInfo]?.remove(processor)
        if (processors[gameInfo]?.isEmpty() == true) {
            processors -= gameInfo
        }
    }

    override fun register(plugin: JavaPlugin, gameProcessorClass: Class<out GameProcessor>) {
        plugin.launch {
            val processor = gameProcessorClass.getConstructor().newInstance()
            kotlin.runCatching {
                processor.onCreated()
            }.exceptionOrNull()?.printStackTrace()
            ProcessorCreatedEvent(processor).callEvent()
            TODO("ㅇㅇ, 셋팅 관련 추가")
        }
    }
}