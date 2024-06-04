package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.ScoreboardUtils.cleanScoreboard
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.data.DungeonBoss
import me.nobaboy.nobaaddons.util.data.DungeonClass
import me.nobaboy.nobaaddons.util.data.DungeonFloor
import me.nobaboy.nobaaddons.util.data.Location
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object DungeonUtils {
    private var currentFloor: DungeonFloor = DungeonFloor.NONE
    private var currentClass: DungeonClass = DungeonClass.EMPTY
    private var currentBoss: DungeonBoss = DungeonBoss.UNKNOWN

    fun checkForDungeonFloor() {
        if (SkyblockUtils.isInLocation(Location.CATACOMBS)) {
            val scoreboard = ScoreboardUtils.getSidebarLines()

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

    fun getPlayerFloor(): DungeonFloor {
        return currentFloor
    }

    fun isInCatacombsFloor(floor: Int): Boolean {
        if (NobaAddons.config.dev.debugMode) return true
        return currentFloor.floor == floor
    }

    fun checkForDungeonClass() {
        if (SkyblockUtils.isInLocation(Location.CATACOMBS)) {
            val playerName = Utils.getPlayerName()
            val players = mc.netHandler.playerInfoMap
            for (player in players) {
                if (player == null || player.displayName == null) continue
                val text = player.displayName.unformattedText
                if (text.contains(playerName) && text.indexOf('(') != -1) {
                    if (text.contains("($playerName)")) continue // Puzzle fail text
                    val dungeonClass = text.substring(text.indexOf("(") + 1, text.lastIndexOf(")"))
                    currentClass = DungeonClass.valueOf(dungeonClass.split(" ")[0].uppercase())
                    break
                }
            }
        } else {
            currentClass = DungeonClass.EMPTY
        }
    }

    fun getPlayerClass(): DungeonClass {
        return currentClass
    }

    @SubscribeEvent
    fun checkForDungeonBoss(event: ClientChatReceivedEvent) {
        val receivedMessage = event.message.unformattedText.cleanString()
        if (SkyblockUtils.isInLocation(Location.CATACOMBS) && receivedMessage.startsWith("[BOSS]"))
            currentBoss = DungeonBoss.fromChat(receivedMessage)
    }

    fun isInPhase(boss: DungeonBoss): Boolean {
        return currentBoss == boss
    }
}