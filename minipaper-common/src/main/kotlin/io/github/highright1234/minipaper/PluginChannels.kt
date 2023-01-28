package io.github.highright1234.minipaper

object PluginChannels {
    const val CHANNEL = "minipaper:main"
    object SubChannels {
        /**
         *
         * player: a joining player
         * structure:
         * game's name : string
         *
         */
        const val JOIN_MINI_GAME = "JoinMiniGame"

        /**
         *
         * player: a joining player
         * structure:
         * processor's unique-id : uuid
         */
        const val JOIN_PROCESSOR = "JoinProcessor"
    }
}