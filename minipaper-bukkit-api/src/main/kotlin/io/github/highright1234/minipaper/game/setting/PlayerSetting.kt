package io.github.highright1234.minipaper.game.setting

data class PlayerSetting(
    var minSize: Int,
    var maxSize: Int,
    var isRejoinable: Boolean,
    var isJoinableInProgress: Boolean,
)