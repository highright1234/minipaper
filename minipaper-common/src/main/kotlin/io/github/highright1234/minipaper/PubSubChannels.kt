package io.github.highright1234.minipaper

import java.util.UUID

object PubSubChannels {
    /**
     * value will be processor's uuid
     */
    private const val PROCESSOR_CREATED = "%s:%s:%s:created"

    /**
     * it's for messaging status
     *
     * first: game-name
     *
     * second: game-version
     *
     * third: processor's uuid
     *
     */
    private const val PROCESSOR_DATA = "%s:%s:%s"

    fun processorCreated(serverName : String, gameInfo: GameInfo) : String =
        PROCESSOR_CREATED.format(serverName, gameInfo.name, gameInfo.version)
    fun processorData(gameInfo: GameInfo, processorUUID: UUID) : String =
        PROCESSOR_DATA.format(gameInfo.name, gameInfo.version, "$processorUUID")

    data class GameInfo(val name: String, val version: String)
}