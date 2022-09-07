package io.github.highright1234.minipaper.game

import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.util.onlinePlayers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID
import kotlin.reflect.KClass

abstract class GameProcessor(val gameInfo: GameInfo) {

    val uuid = UUID.randomUUID()!!
    var listeners = listOf<KClass<out Listener>>()

    init {
        if (MiniPaper.runningGameProcessor != null)
            throw IllegalStateException("Running GameProcessor must be one")
    }

    val scoreboard = Bukkit.getScoreboardManager().newScoreboard

    private lateinit var _plugin : JavaPlugin
    val plugin get() = _plugin

    var players = listOf<UUID>()
    private set

    val onlinePlayers get() = players.onlinePlayers

    var isStarted = false
    private set

    private lateinit var _scope : CoroutineScope
    val scope : CoroutineScope get() = _scope

    private fun setup(plugin: JavaPlugin) {
        _plugin = plugin
        _scope = MiniPaper.coroutineManager.scopeOf(this)
    }

    suspend fun start() {
        kotlin.runCatching {
            onStart()
        }.exceptionOrNull()?.printStackTrace()
        TODO()
    }
    suspend fun stop() {
        scope.cancel()
        kotlin.runCatching {
            onStop()
        }.exceptionOrNull()?.printStackTrace()
        TODO()
    }
    suspend fun delete() {
        kotlin.runCatching {
            onDeleted()
        }.exceptionOrNull()?.printStackTrace()
    }

    operator fun plusAssign(players: Collection<Player>) = players.toList().forEach(::addPlayer)
    operator fun minusAssign(players: Collection<Player>) = players.toList().forEach(::removePlayer)
    operator fun plusAssign(player: Player) = addPlayer(player)
    operator fun minusAssign(player: Player) = removePlayer(player)
    operator fun contains(player: Player) = players.onlinePlayers.contains(player)
    operator fun contains(uuid: UUID) = players.contains(uuid)

    fun addPlayer(player: Player) {
        if (player.uniqueId in players) return
        players = players.plus(player.uniqueId)
    }

    fun removePlayer(player: Player) {
        if (player.uniqueId !in players) return
        players = players.minus(player.uniqueId)
    }

    open suspend fun onCreated() {}
    open suspend fun onStart() {}
    open suspend fun onStop() {}
    open suspend fun onDeleted() {}

    fun registerListener(listener: Listener) =
        MiniPaper.eventManger.registerListener(this, listener)

    fun <T: Event> listen(clazz : Class<T>) : Flow<T> =
        MiniPaper.eventManger.listen(this, clazz)

}