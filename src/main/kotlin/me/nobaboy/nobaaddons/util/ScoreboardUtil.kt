package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.StringUtils

object ScoreboardUtil {
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

        return lines
    }
}