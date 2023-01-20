package io.github.highright1234.minipaper

object PluginChannels {
    const val CHANNEL = "minipaper:main"
    object SubChannels {
        /**
         *
         * when bungee send:
         * player: a joining player
         * structure:
         * empty
         *
         * when bukkit send:
         * player: a joining player
         * structure:
         * game's name : string
         *
         */
        const val JOIN_MINI_GAME = "JoinMiniGame"
    }
}