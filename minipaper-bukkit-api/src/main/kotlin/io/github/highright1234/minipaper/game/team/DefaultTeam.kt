package io.github.highright1234.minipaper.game.team

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

sealed class DefaultTeam(val color: NamedTextColor, val name: String) {

    companion object {
        val all = this::class.nestedClasses.map { it.objectInstance as DefaultTeam }
    }
    // based on bed-wars
    // 16
    object RED: DefaultTeam(NamedTextColor.RED, "RED")
    object BLUE: DefaultTeam(NamedTextColor.BLUE, "BLUE")
    object GREEN: DefaultTeam(NamedTextColor.GREEN, "GREEN")
    object YELLOW: DefaultTeam(NamedTextColor.YELLOW, "YELLOW")
    object AQUA: DefaultTeam(NamedTextColor.AQUA, "AQUA")
    object WHITE: DefaultTeam(NamedTextColor.WHITE, "WHITE")
    object PINK: DefaultTeam(NamedTextColor.LIGHT_PURPLE, "PINK")
    object GRAY: DefaultTeam(NamedTextColor.GRAY, "GRAY")
    object DARK_RED: DefaultTeam(NamedTextColor.DARK_RED, "DARK_RED")
    object DARK_BLUE: DefaultTeam(NamedTextColor.DARK_BLUE, "DARK_BLUE")
    object DARK_GREEN: DefaultTeam(NamedTextColor.DARK_GREEN, "DARK_GREEN")
    object GOLD: DefaultTeam(NamedTextColor.GOLD, "GOLD")
    object DARK_AQUA: DefaultTeam(NamedTextColor.DARK_AQUA, "DARK_AQUA")
    object DARK_GRAY: DefaultTeam(NamedTextColor.DARK_GRAY, "DARK_GRAY")
    object PURPLE: DefaultTeam(NamedTextColor.DARK_PURPLE, "PURPLE")
    object BLACK: DefaultTeam(NamedTextColor.BLACK, "BLACK")

}