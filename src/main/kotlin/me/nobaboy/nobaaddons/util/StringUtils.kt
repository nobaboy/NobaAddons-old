package me.nobaboy.nobaaddons.util

import net.minecraft.util.StringUtils

object StringUtils {
    infix fun String.lowercaseEquals(other: String) = this.lowercase() == other.lowercase()

    infix fun String.lowercaseContains(other: String) = this.lowercase().contains(other.lowercase())

    fun String.startsWith(list: List<String>): Boolean = list.any { this.startsWith(it) }

    fun String.capitalizeFirstLetters(): String {
        return this.split(" ").joinToString("") {
            if (it.contains("/")) {
                it.split("/").joinToString("/") { word ->
                    word.replaceFirstChar { firstChar -> firstChar.uppercase() }
                }
            } else {
                it.replaceFirstChar { firstChar -> firstChar.uppercase() }
            }
        }
    }

    fun String.cleanString(): String {
        return StringUtils.stripControlCodes(this)
    }
}