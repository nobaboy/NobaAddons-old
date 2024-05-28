package me.nobaboy.nobaaddons.util

import java.util.regex.Matcher
import java.util.regex.Pattern

object RegexUtils {
    fun Pattern.matches(string: String?) = string?.let { matcher(it).matches() } ?: false

    inline fun <T> Pattern.matchMatcher(text: String, consumer: Matcher.() -> T) =
        matcher(text).let { if (it.matches()) consumer(it) else null }

    inline fun <T> List<String>.matchFirst(pattern: Pattern, consumer: Matcher.() -> T): T? {
        for (line in this) {
            pattern.matcher(line).let { if (it.matches()) return consumer(it) }
        }
        return null
    }
}