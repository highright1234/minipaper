package io.github.highright1234.minipaper.isolation

import io.github.highright1234.shotokonoko.config.ConfigFrame
import io.github.monun.tap.config.Config
import org.bukkit.Bukkit
import org.bukkit.entity.Player

// TODO config
abstract class IsolationSetting: ConfigFrame() {

    abstract val bypassPermission: String

    val bypasses: Iterable<Player> get() = Bukkit.getOnlinePlayers().filter { it.hasPermission(bypassPermission) }

    @Config
    open var isEnabled: Boolean = true

    abstract suspend fun init()


}