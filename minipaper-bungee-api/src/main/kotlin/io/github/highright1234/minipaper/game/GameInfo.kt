package io.github.highright1234.minipaper.game

interface GameInfo {
    val name: String
    val processors: Collection<GameProcessorInfo>
}