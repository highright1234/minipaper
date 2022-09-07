package io.github.highright1234.minipaper.internal

import com.github.shynixn.mccoroutine.bukkit.launch
import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.bungee.PluginMessageUtil
import io.github.highright1234.minipaper.coroutine.CoroutineManager
import io.github.highright1234.minipaper.game.GameManager
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.game.event.GameEventManager
import io.github.highright1234.minipaper.internal.bungee.PluginMessageUtilImpl
import io.github.highright1234.minipaper.internal.coroutine.CoroutineManagerImpl
import io.github.highright1234.minipaper.internal.game.GameManagerImpl
import io.github.highright1234.minipaper.internal.game.event.GameEventManagerImpl
import kotlinx.coroutines.withTimeout
import org.bukkit.plugin.java.JavaPlugin

object MiniPaperImpl : MiniPaper {

    lateinit var plugin: JavaPlugin
    override val pluginMessageUtil: PluginMessageUtil = PluginMessageUtilImpl
    override val eventManger: GameEventManager = GameEventManagerImpl
    override val coroutineManager: CoroutineManager = CoroutineManagerImpl
    override val gameManager: GameManager = GameManagerImpl
    override var runningGameProcessor: GameProcessor? = null
    private set

    override fun register(plugin: JavaPlugin, gameProcessorClass: Class<out GameProcessor>) {
        if (runningGameProcessor == null) throw IllegalStateException(
            "Running GameProcessor must be one"
        )
        val processor = gameProcessorClass.getConstructor().newInstance()
        processor.setup(plugin)
        runningGameProcessor = processor
        plugin.launch {
            processor.onCreated()
        }
    }

    private fun GameProcessor.setup(parent: JavaPlugin) {
        this::class.java
            .getDeclaredMethod("setup", JavaPlugin::class.java)
            .invoke(this, parent)
    }
}