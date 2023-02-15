package io.github.highright1234.minipaper.game

import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.game.setting.*
import org.bukkit.plugin.java.JavaPlugin
import java.util.Collections
import kotlin.reflect.KProperty

class GameInfo private constructor(
    name: String,
    plugin: JavaPlugin,
    version: String,
) {

    val name: String
    val plugin: JavaPlugin
    val version: String

    init {
        this.name = name
        this.plugin = plugin
        this.version = version
    }

    companion object {
        internal val games = Collections.synchronizedList(mutableListOf<GameInfo>())

        fun get(name: String): GameInfo? = games.filter { it.name == name }.maxByOrNull { it.version.versionToInt() }

        fun make(name: String, plugin: JavaPlugin, version: String? = null, builder: GameInfo.() -> Unit): GameInfo {
            val gameInfo = GameInfo(name, plugin, version ?: plugin.description.version)
            get(name)?.also {
                if (it.plugin != plugin) throw IllegalStateException("There shouldn't be a game with the same name.")
            }.takeIf { it == gameInfo }?.let { return it }

            gameInfo.apply(builder)
            require(gameInfo::playerSetting.isInitialized && gameInfo::worldSetting.isInitialized) {
                "playerSetting and worldSetting should be initialized!"
            }
            return gameInfo
        }
    }

    val processors: Collection<GameProcessor> get() = MiniPaper.gameManager.gameProcessors[this] ?: emptyList()

    // YOU MUST SET THESE VALUES
    lateinit var playerSetting: PlayerSetting // TODO 기능
    lateinit var worldSetting: WorldSetting // TODO 기능

    var autoSetting = AutoSetting() // TODO 기능
    var processorSetting = ProcessorSetting()

    var teamSetting: TeamSetting? = null



    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): GameDatastore {
        return getProperty(property.name)
    }

    fun getProperty(name: String): GameDatastore = MiniPaper.gameManager.getGameDatastore(this, name)






    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameInfo

        if (name != other.name) return false
        if (plugin != other.plugin) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + plugin.hashCode()
        return result
    }


}





private fun String.versionToInt(): Double {
    var out = 0.0
    val information = split(".")
    out += information[0].toInt() * 1_000_000
    out += information[1].toInt() * 1_000
    var last = information[2]
    if (last.uppercase().endsWith("-SNAPSHOT")) {
        out += 0.5
        last = last.removeSuffix("-SNAPSHOT")
    }
    out += last.toInt()
    return out
}
