package io.github.highright1234.shotokonoko.internal

import io.github.highright1234.shotokonoko.game.GameProcessor
import io.github.highright1234.shotokonoko.Shotokonoko
import io.github.highright1234.shotokonoko.bungee.PluginMessageUtil
import io.github.highright1234.shotokonoko.coroutine.CoroutineManager
import io.github.highright1234.shotokonoko.game.event.GameEventManager
import io.github.highright1234.shotokonoko.internal.bungee.PluginMessageUtilImpl
import io.github.highright1234.shotokonoko.internal.coroutine.CoroutineManagerImpl
import io.github.highright1234.shotokonoko.internal.game.event.GameEventManagerImpl
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.bukkit.plugin.java.JavaPlugin

object ShotokonokoImpl : Shotokonoko {

    lateinit var plugin: JavaPlugin
    override var pluginMessageUtil: PluginMessageUtil = PluginMessageUtilImpl
    internal set
    override val eventManger: GameEventManager = GameEventManagerImpl
    override val coroutineManager: CoroutineManager = CoroutineManagerImpl
    override var runningGameProcessor: GameProcessor? = null
    private set

    override fun register(plugin: JavaPlugin, gameProcessorClass: Class<out GameProcessor>) {
        if (runningGameProcessor == null) throw IllegalStateException(
            "Running GameProcessor must be one"
        )
        val processor = gameProcessorClass.getConstructor().newInstance()
        processor.setup(plugin)
        runningGameProcessor = processor
        processor.synchronousScope.launch {
            withTimeout(1000L) {
                processor.onCreated()
            }
        }
    }

    private fun GameProcessor.setup(parent: JavaPlugin) {
        this::class.java
            .getDeclaredMethod("setup", JavaPlugin::class.java)
            .invoke(this, parent)
    }
}