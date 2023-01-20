package io.github.highright1234.minipaper

import io.github.highright1234.minipaper.game.GameManager

private val minipaper: MiniPaper by lazy { loader() }

interface MiniPaper {

    companion object: MiniPaper by minipaper

    val gameManager: GameManager
    val queueProcessor: QueueProcessor
}

// "io.github.highright1234.minipaper"
private val packageName = "io.github.highright1234.minipaper"

internal inline fun <reified T> loader() =
    T::class.java.packageName
        .removePrefix(packageName)
        .let {
            "$packageName.${it}internal.${T::class.java.simpleName}Impl"
        }
        .let { Class.forName(it).kotlin }
        .objectInstance as T