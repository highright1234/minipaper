package io.github.highright1234.minipaper.game

import com.google.common.reflect.ClassPath
import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.event.*
import io.github.highright1234.minipaper.event.processor.*
import io.github.highright1234.minipaper.game.event.GameListener
import io.github.highright1234.minipaper.game.provider.DefaultDeletionProvider
import io.github.highright1234.minipaper.game.provider.DeletionProvider
import io.github.highright1234.minipaper.game.team.GameTeam
import io.github.highright1234.minipaper.util.onlinePlayers
import io.github.highright1234.minipaper.util.scoreboard.SmartScoreboard
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class GameProcessor {
    @Suppress("Unused")
    private fun init(gameInfo: GameInfo) {
        _gameInfo = gameInfo
    }

    private lateinit var _gameInfo: GameInfo
    val gameInfo: GameInfo get() = _gameInfo
    val uniqueId = UUID.randomUUID()!!
    val world get() = worlds.first()

    open val isJoinable
    get() = gameInfo.playerSetting.maxSize > players.size &&
            (!isStarted || gameInfo.playerSetting.isJoinableInProgress)

    internal val _world = mutableListOf<World>()
    val worlds: List<World> get() = _world.toList()

    open var deletionProvider: DeletionProvider<GameProcessor>? = DefaultDeletionProvider

    open var scoreboard: SmartScoreboard? = null

    val plugin = gameInfo.plugin

    private val _players = ArrayList<UUID>()
    val players get() = _players.toList()

    val onlinePlayers get() = players.onlinePlayers

    var isStarted = false
        private set

    val scope: CoroutineScope get() = MiniPaper.coroutineManager.scopeOf(this)
    val minecraftDispatcher: CoroutineContext get() = MiniPaper.coroutineManager.minecraftDispatcherOf(this)
    val asyncDispatcher: CoroutineContext get() = MiniPaper.coroutineManager.asyncDispatcherOf(this)

    internal val _team = arrayListOf<GameTeam>()
    open val teams: List<GameTeam> get() = _team.toList()

    fun getProperty(name: String): GameDatastore = gameInfo.getProperty(name)


    suspend fun start() {

        gameInfo.teamSetting?.createTeam(this)

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
        require(isJoinable) { "$uniqueId ${gameInfo.name} is not join-able" }
        if (!player.isOnline) return
        if (player.uniqueId in players) return
        _players += player.uniqueId

        ProcessorPlayerJoinEvent(this@GameProcessor, player).callEvent()

        if (gameInfo.autoSetting.autoStart && players.size >= gameInfo.playerSetting.minSize) {
            scope.launch(minecraftDispatcher) {
                start()
            }
        }

        // 플레이어 데이터 관련 처리
        // 에: 이밴트 키기
        // 이게 뭔소리야
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


    fun <T : Event> listenEvents(
        clazz: Class<T>,
        listeningAllEvent: Boolean = false,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
    ): Flow<T> = MiniPaper.eventManger.listenEvents(
        this,
        clazz,
        listeningAllEvent,
        priority,
        ignoreCancelled
    )
}
