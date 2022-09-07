package io.github.highright1234.minipaper.internal

import io.github.highright1234.minipaper.QueueProcessor
import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.game.GameManager
import io.github.highright1234.minipaper.internal.game.GameManagerImpl

object MiniPaperImpl: MiniPaper {
    override val gameManager: GameManager = GameManagerImpl
    override val queueProcessor: QueueProcessor = QueueProcessorImpl
}