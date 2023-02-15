package io.github.highright1234.minipaper.game

import io.github.highright1234.minipaper.game.team.GameTeam
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*


/**
 *
 * 자동처리 리스트
 * 플레이어 나가면 자동 eliminate
 *
 * 뭔가 더 처리해야하는거 있었는데 까먹음
 */
abstract class EliminatableGameProcessor: GameProcessor() {

    private val _eliminatedPlayers = mutableListOf<UUID>()
    val eliminatedPlayers: List<UUID> get() = _eliminatedPlayers.toList()


    var winner: UUID? = null
    var winningTeam: GameTeam? = null

    open var isWinnerMustBeOnline: Boolean = true

    fun eliminatePlayer(player: Player) = eliminatePlayer(player.uniqueId)

    fun eliminatePlayer(uniqueId: UUID) {
        _eliminatedPlayers -= uniqueId
        if (gameInfo.teamSetting == null && playingUsers.size == 1) {
            val finalPlayer = playingUsers.first()
            if (isWinnerMustBeOnline && Bukkit.getPlayer(finalPlayer) == null) {
                // It will not be thrown
                // caused by side effects
                IllegalStateException("The winner is not online").printStackTrace()
            }
            winner = finalPlayer
        } else if (playingTeams.size == 1) {
            winningTeam = playingTeams.first()
        }
        scope.launch {
            onEliminated(uniqueId)
            stop()
        }
    }

    open suspend fun onEliminated(uniqueId: UUID) {  }

    val playingTeams get() = teams.filter { (it.players - eliminatedPlayers).isNotEmpty() }
    val playingUsers get() = players - eliminatedPlayers

}