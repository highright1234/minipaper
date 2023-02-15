package io.github.highright1234.minipaper.internal

import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.config.MiniPaperConfig
import io.github.highright1234.minipaper.coroutine.CoroutineManager
import io.github.highright1234.minipaper.game.GameManager
import io.github.highright1234.minipaper.game.event.GameEventManager
import io.github.highright1234.minipaper.internal.coroutine.CoroutineManagerImpl
import io.github.highright1234.minipaper.internal.game.GameManagerImpl
import io.github.highright1234.minipaper.internal.game.event.GameEventManagerImpl
import io.github.highright1234.minipaper.internal.queue.QueueManagerImpl
import io.github.highright1234.minipaper.game.queue.QueueManager
import io.github.highright1234.minipaper.util.world.WorldUtil
import org.bukkit.plugin.java.JavaPlugin

object MiniPaperImpl : MiniPaper {

    internal fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        GameManagerImpl.init()
    }
    lateinit var plugin: JavaPlugin
    override val gameManager: GameManager = GameManagerImpl
    override val queueManager: QueueManager = QueueManagerImpl
    override val eventManger: GameEventManager = GameEventManagerImpl
    override val coroutineManager: CoroutineManager = CoroutineManagerImpl

    override val isDebug: Boolean get() = MiniPaperConfig.isDebug

    override lateinit var worldUtil: WorldUtil

}