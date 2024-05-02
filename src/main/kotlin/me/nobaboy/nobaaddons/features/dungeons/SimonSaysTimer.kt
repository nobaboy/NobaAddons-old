package me.nobaboy.nobaaddons.features.dungeons

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.api.PartyAPI
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.DungeonUtils
import me.nobaboy.nobaaddons.util.LocationUtils
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.StringUtils.matchMatcher
import me.nobaboy.nobaaddons.util.Utils
import me.nobaboy.nobaaddons.util.data.DungeonBoss
import me.nobaboy.nobaaddons.util.data.Location
import net.minecraft.block.BlockButtonStone
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.IOException
import java.util.regex.Pattern

class SimonSaysTimer {
    private val chatPattern: Pattern = Pattern.compile("^(?<username>[A-z0-9_]+) completed a device! \\([1-7]/7\\)")
    private var deviceDone: Boolean = false
    private var firstButtonPressed: Boolean = false
    private var deviceStartTime: Long = 0
    private var timeTakenToEnd: Double = 0.0

    private fun processDeviceTime() {
        deviceDone = true

        timeTakenToEnd = (System.currentTimeMillis() - deviceStartTime).toDouble() / 1000
        val times: MutableList<Double> = SSFile.times
        times.add(timeTakenToEnd)

        var personalBest: Double? = SSFile.personalBest
        if (personalBest == null || timeTakenToEnd < personalBest) {
            SSFile.personalBest = timeTakenToEnd
            personalBest = timeTakenToEnd
        }

        val isPB = if (timeTakenToEnd <= personalBest) " §3§l(PB)" else " §3($personalBest)"
        val message = "Simon Says took ${timeTakenToEnd}s to complete. $isPB"
        if (NobaAddons.config.ssDeviceTimerPC && PartyAPI.inParty) {
            ChatUtils.queueCommand("pc ${StringUtils.stripControlCodes(message)}")
        } else {
            ChatUtils.addMessage(message)
        }

        try {
            SSFile.save()
        } catch (ex: IOException) {
            NobaAddons.LOGGER.error("Failed to save new time/personal best", ex)
        }
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        if (deviceDone || firstButtonPressed) {
            deviceDone = false
            firstButtonPressed = false
            deviceStartTime = 0
            timeTakenToEnd = 0.0
        }
    }

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (!isEnabled()) return
        if (deviceDone) return
        if (!DungeonUtils.isInCatacombs7()) return
        if (!DungeonUtils.isInPhase(DungeonBoss.GOLDOR)) return

        val receivedMessage = event.message.unformattedText.cleanString()

        chatPattern.matchMatcher(receivedMessage) {
            val username = group("username")

            if (username != Utils.getPlayerName()) {
                deviceDone = true
                return
            }

            processDeviceTime()
        }
    }

    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) { // Very unreadable but bare with it.
        if (!isEnabled()) return
        if (firstButtonPressed) return
        if (deviceDone) return
        if (event.entityPlayer != mc.thePlayer) return
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return

        val blockState = event.world.getBlockState(event.pos) ?: return
        if (blockState.block == null) return
        if (blockState.block !is BlockButtonStone) return

        firstButtonPressed = true
        deviceStartTime = System.currentTimeMillis()
    }

    fun isEnabled() = NobaAddons.config.ssDeviceTimer && LocationUtils.isInLocation(Location.CATACOMBS)
}