package io.github.highright1234.minipaper.internal.coroutine

import io.github.highright1234.minipaper.MiniPaperPlugin
import io.github.highright1234.minipaper.game.GameProcessor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

class SynchronousDispatcher(private val gameProcessor: GameProcessor) : CoroutineDispatcher() {
    init {

    }
    override fun dispatch(context: CoroutineContext, block: Runnable) {

    }
}