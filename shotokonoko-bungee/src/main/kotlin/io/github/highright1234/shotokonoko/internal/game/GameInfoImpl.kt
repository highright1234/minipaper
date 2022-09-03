package io.github.highright1234.shotokonoko.internal.game

import io.github.highright1234.shotokonoko.game.GameInfo
import io.github.highright1234.shotokonoko.game.GameProcessorInfo

class GameInfoImpl(
    override val name: String,
) : GameInfo {
    override val processors: Collection<GameProcessorInfo> = emptyList()
}
