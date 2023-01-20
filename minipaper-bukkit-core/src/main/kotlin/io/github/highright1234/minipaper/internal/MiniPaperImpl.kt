package io.github.highright1234.minipaper.internal

import com.github.shynixn.mccoroutine.bukkit.launch
import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.bungee.PluginMessageUtil
import io.github.highright1234.minipaper.config.MiniPaperConfig
import io.github.highright1234.minipaper.coroutine.CoroutineManager
import io.github.highright1234.minipaper.event.ProcessorCreatedEvent
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.game.event.GameEventManager
import io.github.highright1234.minipaper.internal.bungee.PluginMessageUtilImpl
import io.github.highright1234.minipaper.internal.coroutine.CoroutineManagerImpl
import io.github.highright1234.minipaper.internal.game.event.GameEventManagerImpl
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.ArrayDeque

object MiniPaperImpl : MiniPaper {

    lateinit var plugin: JavaPlugin
    override val pluginMessageUtil: PluginMessageUtil = PluginMessageUtilImpl
    override val eventManger: GameEventManager = GameEventManagerImpl
    override val coroutineManager: CoroutineManager = CoroutineManagerImpl
    override val isDebug: Boolean get() = MiniPaperConfig.isDebug
    override var runningGameProcessor: GameProcessor? = null
    private set

    override val processorQueue = LinkedList<Class<out GameProcessor>>()

    override fun register(plugin: JavaPlugin, gameProcessorClass: Class<out GameProcessor>) {
        require(runningGameProcessor == null) { throw IllegalStateException("Running GameProcessor must be one") }
        val processor = gameProcessorClass.getConstructor().newInstance()
        runningGameProcessor = processor
        plugin.launch {
            kotlin.runCatching {
                processor.onCreated()
            }.exceptionOrNull()?.printStackTrace()
            ProcessorCreatedEvent(processor).callEvent()
        }
    }
}