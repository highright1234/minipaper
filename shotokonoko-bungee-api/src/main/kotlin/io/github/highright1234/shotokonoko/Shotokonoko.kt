package io.github.highright1234.shotokonoko

import io.github.highright1234.shotokonoko.game.GameManager

interface Shotokonoko {
    val gameManager: GameManager
    val queueProcessor: QueueProcessor
}