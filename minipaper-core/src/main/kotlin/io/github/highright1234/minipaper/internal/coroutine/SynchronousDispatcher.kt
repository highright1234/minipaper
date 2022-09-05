package io.github.highright1234.minipaper.internal.coroutine

import io.github.highright1234.minipaper.MiniPaperPlugin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.bukkit.Bukkit
import kotlin.coroutines.CoroutineContext

object SynchronousDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (Bukkit.getServer().isPrimaryThread) {
            block.run()
        } else {
            Bukkit.getScheduler().runTask(MiniPaperPlugin.plugin, block)
        }
    }
}