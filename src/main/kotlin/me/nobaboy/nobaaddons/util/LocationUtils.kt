package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.ScoreboardUtils.cleanScoreboard
import me.nobaboy.nobaaddons.util.data.Location
import net.minecraft.client.Minecraft

object LocationUtils {
    var inSkyblock: Boolean = false
    private var currentLocation: Location = Location.NONE

    fun checkForSkyblock() {
        if (NobaAddons.config.dev.debugMode) {
            inSkyblock = true
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
            val players = Minecraft.getMinecraft().netHandler.playerInfoMap
            for (player in players) {
                if (player == null || player.displayName == null) continue
                val text = player.displayName.unformattedText
                if (text.startsWith("Area: ") || text.startsWith("Dungeon: ")) {
                    currentLocation = Location.fromTab(text.substring(text.indexOf(":") + 2))
                    return
                }
            }
        }
        currentLocation = Location.NONE
    }

    fun isInLocation(location: Location): Boolean {
        if (NobaAddons.config.dev.debugMode) return true
        return currentLocation == location
    }
}