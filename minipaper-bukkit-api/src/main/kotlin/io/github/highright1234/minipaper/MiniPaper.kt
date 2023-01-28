package io.github.highright1234.minipaper

import io.github.highright1234.minipaper.coroutine.CoroutineManager
import io.github.highright1234.minipaper.game.GameManager
import io.github.highright1234.minipaper.game.event.GameEventManager
import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.util.world.WorldUtil
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.Queue

private val minipaper: MiniPaper by lazy { loader() }
@Suppress("SpellCheckingInspection")
internal val plugin: JavaPlugin get() = Bukkit.getPluginManager().getPlugin("Minipaper") as JavaPlugin

interface MiniPaper {
    companion object: MiniPaper by minipaper

    val isDebug: Boolean

    val gameManager: GameManager
    val eventManger: GameEventManager
    val coroutineManager: CoroutineManager

    val worldUtil: WorldUtil

}

// "io.github.highright1234.minipaper"
private val packageName = "io.github.highright1234.minipaper"
internal inline fun <reified T> loader() =
    T::class.java.packageName
        .removePrefix(packageName)
        .let {
            "$packageName.${it}internal.${T::class.java.simpleName}Impl"
        }
        .let { Class.forName(it).kotlin }
        .objectInstance as T