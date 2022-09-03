package io.github.highright1234.shotokonoko.internal.game

import io.github.highright1234.shotokonoko.game.GameInfo
import io.github.highright1234.shotokonoko.game.GameProcessorInfo
import net.md_5.bungee.api.config.ServerInfo
import java.util.*

data class GameProcessorInfoImpl(
    override val game: GameInfo,
    override val uuid: UUID,
    override val owner: ServerInfo
) : GameProcessorInfo