package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.StringUtils.lowercaseContains
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.StringUtils

object ScoreboardUtils {
    private val splitIcons = listOf(
        "\uD83C\uDF6B",
        "\uD83D\uDCA3",
        "\uD83D\uDC7D",
        "\uD83D\uDD2E",
        "\uD83D\uDC0D",
        "\uD83D\uDC7E",
        "\uD83C\uDF20",
        "\uD83C\uDF6D",
        "⚽",
        "\uD83C\uDFC0",
        "\uD83D\uDC79",
        "\uD83C\uDF81",
        "\uD83C\uDF89",
        "\uD83C\uDF82",
        "\uD83D\uDD2B",
    )

    fun String.cleanScoreboard() = StringUtils.stripControlCodes(this).filter { it.code in (21 until 127) }

    fun getSidebarLines(): List<String> {
        val lines = mutableListOf<String>()
        if (mc.theWorld == null) return lines
        val scoreboard = mc.theWorld.scoreboard ?: return lines
        val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return lines

        try {
            val scores = scoreboard.getSortedScores(objective)
            val filteredScores = scores.filter { it != null && it.playerName != null && !it.playerName.startsWith("#") }
            val limitedScores = if (filteredScores.size > 15) filteredScores.takeLast(15) else filteredScores
            for (score in limitedScores) {
                val team = scoreboard.getPlayersTeam(score.playerName)
                lines.add(ScorePlayerTeam.formatPlayerName(team, score.playerName))
            }
        } catch (e: ConcurrentModificationException) {
            e.printStackTrace()
            return emptyList()
        }

        lines.replaceAll { line ->
            var modifiedLine = line
            splitIcons.forEach { icon ->
                modifiedLine = modifiedLine.replace(icon, "")
            }
            modifiedLine
        }

        return lines
    }

    fun isInScoreboard(texts: List<String>?): Boolean {
        val scoreboard = getSidebarLines()
        scoreboard.forEach { line ->
            val cleanLine = line.cleanString()
            texts?.forEach { text ->
                if (cleanLine.lowercaseContains(text)) return true
            }
        }
        return false
    }

    fun isInScoreboard(text: String): Boolean {
        val scoreboard = getSidebarLines()
        scoreboard.forEach { line ->
            if (line.lowercaseContains(text)) return true
        }
        return false
    }
}