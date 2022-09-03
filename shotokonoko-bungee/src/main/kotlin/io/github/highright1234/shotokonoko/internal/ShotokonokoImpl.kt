package io.github.highright1234.shotokonoko.internal

import io.github.highright1234.shotokonoko.QueueProcessor
import io.github.highright1234.shotokonoko.Shotokonoko
import io.github.highright1234.shotokonoko.game.GameManager
import io.github.highright1234.shotokonoko.internal.game.GameManagerImpl

object ShotokonokoImpl: Shotokonoko {
    override val gameManager: GameManager = GameManagerImpl
    override val queueProcessor: QueueProcessor = QueueProcessorImpl
}