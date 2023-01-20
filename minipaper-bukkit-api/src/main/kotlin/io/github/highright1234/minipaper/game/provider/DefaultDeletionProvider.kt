package io.github.highright1234.minipaper.game.provider

import io.github.highright1234.minipaper.game.GameProcessor
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

object DefaultDeletionProvider: DeletionProvider<GameProcessor> {
    override suspend fun onStop(gameProcessor: GameProcessor) {
        delay(5.seconds)
        gameProcessor.delete()
    }
}