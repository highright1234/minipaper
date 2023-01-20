package io.github.highright1234.minipaper.game

import com.google.common.reflect.ClassPath
import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.event.*
import io.github.highright1234.minipaper.game.event.GameListener
import io.github.highright1234.minipaper.game.provider.DefaultDeletionProvider
import io.github.highright1234.minipaper.game.provider.DeletionProvider
import io.github.highright1234.minipaper.game.team.GameTeam
import io.github.highright1234.minipaper.game.team.maker.TeamMaker
import io.github.highright1234.minipaper.util.onlinePlayers
import io.github.highright1234.minipaper.util.scoreboard.SmartScoreboard
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import java.util.UUID
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

abstract class GameProcessor(val gameInfo: GameInfo) {

    val uuid = UUID.randomUUID()!!
    var listeners = listOf<KClass<out Listener>>()
    val world get() = worlds.first()

    internal val _world = mutableListOf<World>()
    val worlds: List<World> get() = _world.toList()

    var deletionProvider: DeletionProvider<GameProcessor>? = DefaultDeletionProvider

    init {
        if (MiniPaper.runningGameProcessor != null)
            throw IllegalStateException("Running GameProcessor must be one")
    }

    var teamCount = 0
        set(value) { field = if ( 0 < value ) value else 0 }

    // TODO
    // 팀관련
    var teamMaker: TeamMaker? = null
    var scoreboard: SmartScoreboard? = null

    val plugin = gameInfo.plugin

    private val _players = ArrayList<UUID>()
    val players get() = _players.toList()

    val onlinePlayers get() = players.onlinePlayers

    var isStarted = false
    private set

    val scope : CoroutineScope get() = MiniPaper.coroutineManager.scopeOf(this)
    val minecraftDispatcher: CoroutineContext get() = MiniPaper.coroutineManager.minecraftDispatcherOf(this)
    val asyncDispatcher: CoroutineContext get() = MiniPaper.coroutineManager.asyncDispatcherOf(this)

    private val _team = arrayListOf<GameTeam>()
    val team: List<GameTeam> get() = _team.toList()

    suspend fun start() {

        if ( 0 < teamCount ) {
            teamMaker?.makeTeam(
                teamCount,
                onlinePlayers,
            )?.let { _team += it }
        }

        withContext(minecraftDispatcher) {
            kotlin.runCatching {
                onStart()
            }.exceptionOrNull()?.printStackTrace()
            ProcessorStartedEvent(this@GameProcessor).callEvent()
        }

        // 시작후 스폰 등등 해야할거임
        // 팀 관련 설정 대충 그런거 있을거임
        TODO()
    }

    suspend fun stop() {
        scope.cancel()
        withContext(minecraftDispatcher) {
            kotlin.runCatching {
                onStop()
            }.exceptionOrNull()?.printStackTrace()
            ProcessorStoppedEvent(this@GameProcessor).callEvent()
        }
        deletionProvider?.onStop(this)
    }

    suspend fun delete() {
        scope.cancel()
        withContext(minecraftDispatcher) {
            kotlin.runCatching {
                onDelete()
            }.exceptionOrNull()?.printStackTrace()
            ProcessorDeletedEvent(this@GameProcessor).callEvent()
        }
    }

    operator fun plusAssign(players: Collection<Player>) = players.toList().forEach(::addPlayer)
    operator fun minusAssign(players: Collection<Player>) = players.toList().forEach(::removePlayer)
    operator fun plusAssign(player: Player) = addPlayer(player)
    operator fun minusAssign(player: Player) = removePlayer(player)
    operator fun contains(player: Player) = players.onlinePlayers.contains(player)
    operator fun contains(uuid: UUID) = players.contains(uuid)

    fun addPlayer(player: Player) {
        if (!player.isOnline) return
        if (player.uniqueId in players) return
        _players += player.uniqueId

        ProcessorPlayerJoinEvent(this@GameProcessor, player).callEvent()
        // 플레이어 데이터 관련 처리
        // 에: 이밴트 키기
        // TODO
    }

    fun removePlayer(player: Player) {
        if (player.uniqueId !in players) return
        _players -= player.uniqueId
        SmartScoreboard.resetPlayer(player)
        ProcessorPlayerQuitEvent(this@GameProcessor, player).callEvent()
    }

    open suspend fun onCreated() {}
    open suspend fun onStart() {}
    open suspend fun onStop() {}
    open suspend fun onDelete() {}

    /**
     *
     */
    fun registerListener(listener: Listener) =
        MiniPaper.eventManger.registerListener(this, listener)

    /**
     *
     */
    @Suppress("UnstableApiUsage")
    fun registerListenersOf(directoryName: String) {
        ClassPath.from(this::class.java.classLoader)
            .getTopLevelClasses("${this::class.java.packageName}.$directoryName")
            .mapNotNull { it.load().asSubclass(Listener::class.java) }
            .map { clazz ->
                val instance = clazz.getConstructor().newInstance() as Listener
                if (instance is GameListener<*>) {
                    @Suppress("UNCHECKED_CAST")
                    (instance as GameListener<GameProcessor>)._gameProcessor = this
                }
                instance
            }.onEach {
                registerListener(it)
            }
    }

    /**
     *
     */
    fun <T: Event> listen(clazz : Class<T>) : Flow<T> =
        MiniPaper.eventManger.listen(this, clazz)
}
