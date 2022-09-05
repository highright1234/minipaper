package io.github.highright1234.minipaper

import io.github.highright1234.minipaper.game.GameManager

interface MiniPaper {
    val gameManager: GameManager
    val queueProcessor: QueueProcessor
}