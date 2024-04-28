package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.ScoreboardUtil.cleanScoreboard
import me.nobaboy.nobaaddons.util.StringUtils.cleanMessage
import me.nobaboy.nobaaddons.util.data.DungeonBoss
import me.nobaboy.nobaaddons.util.data.DungeonClass
import me.nobaboy.nobaaddons.util.data.DungeonFloor
import me.nobaboy.nobaaddons.util.data.Location
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object Utils {
    var inSkyblock: Boolean = false
    var currentLocation: Location = Location.NONE
    var currentFloor: DungeonFloor = DungeonFloor.NONE
    var currentClass: DungeonClass = DungeonClass.NONE
    var currentBoss: DungeonBoss = DungeonBoss.UNKNOWN

    fun checkForSkyblock() {
        if (NobaAddons.config.debugMode) {
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
        return currentLocation == location
    }

    fun checkForDungeonFloor() {
        if (isInLocation(Location.CATACOMBS)) {
            val scoreboard = ScoreboardUtil.getSidebarLines()

            for (s in scoreboard) {
                val sCleaned = s.cleanScoreboard()

                if (sCleaned.contains("The Catacombs (")) {
                    val floor = sCleaned.substring(sCleaned.indexOf("(") + 1, sCleaned.indexOf(")"))

                    try {
                        currentFloor = DungeonFloor.valueOf(floor)
                    } catch (ex: IllegalArgumentException) {
                        currentFloor = DungeonFloor.NONE
                        NobaAddons.LOGGER.error("Unexpected value for Dungeon Floor: ", ex)
                    }

                    break
                }
            }
        } else {
            currentFloor = DungeonFloor.NONE
        }
    }

    fun isInCatacombs7(): Boolean {
        return currentFloor == DungeonFloor.F7 || currentFloor == DungeonFloor.M7
    }

    fun checkForDungeonClass() {
        if (isInLocation(Location.CATACOMBS)) {
            val playerName = Minecraft.getMinecraft().thePlayer.name
            val players = Minecraft.getMinecraft().netHandler.playerInfoMap
            for (player in players) {
                if (player == null || player.displayName == null) continue
                val text = player.displayName.unformattedText
                if (text.contains(playerName) && text.indexOf('(') != -1) {
                    val dungeonClass = text.substring(text.indexOf("(") + 1, text.lastIndexOf(")"))
                    if (dungeonClass == "EMPTY") return
                    currentClass = DungeonClass.valueOf(dungeonClass.split(" ")[0].uppercase())
                    break
                }
            }
        } else {
            currentClass = DungeonClass.NONE
        }
    }

    @SubscribeEvent
    fun checkForDungeonBoss(event: ClientChatReceivedEvent) {
        val receivedMessage = event.message.unformattedText.cleanMessage()
        if (isInLocation(Location.CATACOMBS) && receivedMessage.startsWith("[BOSS]"))
            currentBoss = DungeonBoss.fromChat(receivedMessage)
    }

    fun isInPhase(boss: DungeonBoss): Boolean {
        return currentBoss == boss
    }

    fun isInScoreboard(text: String?): Boolean {
        val scoreboard = ScoreboardUtil.getSidebarLines()
        for (s in scoreboard) {
            val sCleaned = ScoreboardUtil.getSidebarLines()
            if (sCleaned.contains(text!!)) return true
        }
        return false
    }

    fun getPlayerName(): String = NobaAddons.mc.thePlayer.name
}