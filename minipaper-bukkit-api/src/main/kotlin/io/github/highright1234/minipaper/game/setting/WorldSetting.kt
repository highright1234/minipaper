package io.github.highright1234.minipaper.game.setting

import io.github.highright1234.minipaper.MiniPaper
import io.github.highright1234.minipaper.util.world.WorldUtil

data class WorldSetting(
    var originalWorlds: List<String>,
    var worldUtil: WorldUtil = MiniPaper.worldUtil,
)