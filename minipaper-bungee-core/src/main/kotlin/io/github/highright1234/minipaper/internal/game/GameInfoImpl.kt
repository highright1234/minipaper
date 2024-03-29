package io.github.highright1234.minipaper.internal.game

import io.github.highright1234.minipaper.game.GameInfo
import io.github.highright1234.minipaper.game.GameProcessorInfo

data class GameInfoImpl(
    override val name: String,
) : GameInfo {
    override val processors: Collection<GameProcessorInfo> get() =
        GameManagerImpl.processorsOf(this)
}
