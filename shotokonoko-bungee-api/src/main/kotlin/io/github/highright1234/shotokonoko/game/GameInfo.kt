package io.github.highright1234.shotokonoko.game

interface GameInfo {
    val name: String
    val processors: Collection<GameProcessorInfo>
}