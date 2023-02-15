package io.github.highright1234.minipaperdebug

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.launch
import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.setting.PlayerSetting
import io.github.highright1234.minipaper.game.setting.WorldSetting
import org.bukkit.plugin.java.JavaPlugin

class MinipaperDebugPlugin : SuspendingJavaPlugin() {
    companion object {
        lateinit var plugin : JavaPlugin
    }

    override suspend fun onEnableAsync() {
        plugin = this
        val game = GameInfo.make("test", plugin) {
            playerSetting = PlayerSetting(2, 2, false, false)
            worldSetting = WorldSetting(listOf("world"))
        }
        launch {
            MiniPaper.gameManager.register(game, TestGameProcessor::class.java)
            logger.info("processor registered")
        }
    }
}