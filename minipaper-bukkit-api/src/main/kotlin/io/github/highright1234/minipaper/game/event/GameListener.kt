package io.github.highright1234.minipaper.game.event

import io.github.highright1234.minipaper.game.GameProcessor
import org.bukkit.event.Listener

abstract class GameListener<T: GameProcessor> : Listener {
    internal lateinit var _gameProcessor : T
    val gameProcessor : T get() = _gameProcessor
}