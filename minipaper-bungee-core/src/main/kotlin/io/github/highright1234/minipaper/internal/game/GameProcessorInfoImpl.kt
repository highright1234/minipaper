package io.github.highright1234.minipaper.internal.game

import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameProcessorInfo
import net.md_5.bungee.api.config.ServerInfo
import java.util.*

data class GameProcessorInfoImpl(
    override val game: GameInfo,
    override val uniqueId: UUID,
    override val owner: ServerInfo
) : GameProcessorInfo