package me.nobaboy.nobaaddons.util

import net.minecraft.util.StringUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtils {
    infix fun String.lowercaseEquals(other: String) = this.lowercase() == other.lowercase()

    infix fun String.lowercaseContains(other: String) = this.lowercase().contains(other.lowercase())

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

    inline fun <T> Pattern.matchMatcher(text: String, consumer: Matcher.() -> T) =
        matcher(text).let { if (it.matches()) consumer(it) else null }
}