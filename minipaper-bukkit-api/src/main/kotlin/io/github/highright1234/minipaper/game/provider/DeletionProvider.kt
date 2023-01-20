package io.github.highright1234.minipaper.game.provider

import io.github.highright1234.minipaper.game.GameProcessor

interface DeletionProvider<T: GameProcessor> {
    suspend fun onStop(gameProcessor: T)
}