package io.github.highright1234.minipaper.game.setting

data class AutoSetting(
    // 게임 시작 플레이어
    var autoStart: Boolean = true,
    var deleteWhenStop: Boolean = true,
)