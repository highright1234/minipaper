package io.github.highright1234.shotokonoko.game

import io.github.highright1234.shotokonoko.Shotokonoko
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

abstract class GameProcessor(val name : String) {

    val uuid = UUID.randomUUID()

    init {
        if (Shotokonoko.runningGameProcessor != null)
            throw IllegalStateException("Running GameProcessor must be one")
    }

    private lateinit var _plugin : JavaPlugin
    val plugin get() = _plugin

    var players = listOf<Player>()
    private set

    var isStarted = false
    private set

    private lateinit var _synchronousScope : CoroutineScope
    val synchronousScope : CoroutineScope get() = _synchronousScope

    suspend fun <R> synchronousScope(block: suspend CoroutineScope.() -> R): R = synchronousScope.block()

    private fun setup(plugin: JavaPlugin) {
        _plugin = plugin
        _synchronousScope = Shotokonoko.coroutineManager.synchronousScopeOf(this)
    }

    suspend fun start() {
        onStart()
        TODO()
    }
    suspend fun stop() {
        onStop()
        TODO()
    }

    operator fun plusAssign(players: Collection<Player>) = players.toList().forEach(::addPlayer)
    operator fun minusAssign(players: Collection<Player>) = players.toList().forEach(::removePlayer)
    operator fun plusAssign(player: Player) = addPlayer(player)
    operator fun minusAssign(player: Player) = removePlayer(player)
    operator fun contains(player: Player) = players.contains(player)

    fun addPlayer(player: Player) {
        if (player in players) return
        players = players.plus(player)
    }

    fun removePlayer(player: Player) {
        if (player !in players) return
        players = players.minus(player)
    }

    open suspend fun onCreated() {}
    open suspend fun onStart() {}
    open suspend fun onStop() {}
    open suspend fun onDeleted() {}

    fun registerListener(listener: Listener) =
        Shotokonoko.eventManger.registerListener(this, listener)

    fun <T: Event> listen(clazz : Class<T>) : Flow<T> =
        Shotokonoko.eventManger.listen(this, clazz)

}