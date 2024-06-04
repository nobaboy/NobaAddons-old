package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.ScoreboardUtils.cleanScoreboard
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.data.Location

object SkyblockUtils {
    var inSkyblock: Boolean = false
    private var currentLocation: Location = Location.NONE

    fun checkForSkyblock() {
        if (NobaAddons.config.dev.debugMode) {
            inSkyblock = mc.theWorld != null
            return
        }

        val mc = NobaAddons.mc
        if (mc.theWorld != null && !mc.isSingleplayer) {
            val scoreboardObj = mc.theWorld.scoreboard.getObjectiveInDisplaySlot(1)
            if (scoreboardObj != null) {
                val scObjName = scoreboardObj.displayName.cleanScoreboard()
                if (scObjName.contains("SKYBLOCK") || scObjName.contains("SKIBLOCK")) {
                    inSkyblock = true
                    return
                }
            }
        }
        inSkyblock = false
    }

    fun checkTabLocation() {
        if (inSkyblock) {
            val players = mc.netHandler.playerInfoMap
            for (player in players) {
                if (player == null || player.displayName == null) continue
                val text = player.displayName.unformattedText.cleanString()
                if (text.startsWith("Area: ") || text.startsWith("Dungeon: ")) {
                    currentLocation = Location.fromTab(text.substring(text.indexOf(":") + 2))
                    return
                }
            }
        }
        currentLocation = Location.NONE
    }

    fun getPlayerLocation(): Location {
        return currentLocation
    }

    fun isInLocation(location: Location): Boolean {
        if (NobaAddons.config.dev.debugMode) return true
        return currentLocation == location
    }
}