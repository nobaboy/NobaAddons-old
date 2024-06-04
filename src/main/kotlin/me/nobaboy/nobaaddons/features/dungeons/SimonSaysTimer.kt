package me.nobaboy.nobaaddons.features.dungeons

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.api.PartyAPI
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile
import me.nobaboy.nobaaddons.util.*
import me.nobaboy.nobaaddons.util.RegexUtils.matchMatcher
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.data.DungeonBoss
import me.nobaboy.nobaaddons.util.data.Location
import net.minecraft.block.BlockButtonStone
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.io.IOException
import java.util.regex.Pattern

class SimonSaysTimer {
    private val config get() = NobaAddons.config.dungeons.simonSaysTimer

    private val deviceFinishPattern: Pattern = Pattern.compile("^(?<username>[A-z0-9_]+) completed a device! \\([1-7]/7\\)")

    private var deviceStartTime = Timestamp.distantPast()
    private var deviceEndTime = Timestamp.distantPast()

    private val stoneButton = BlockPos(110, 121, 91)
    private var playerDoingDevice: String? = null
    private var firstButtonPressed: Boolean = false
    private var deviceDone: Boolean = false

    private fun processPersonalTime() {
        val times: MutableList<Double> = SSFile.times
        val timeTakenToEnd = (deviceEndTime.toMillis() - deviceStartTime.toMillis()).toDouble() / 1000
        times.add(timeTakenToEnd)

        var personalBest: Double? = SSFile.personalBest
        if (personalBest == null || timeTakenToEnd < personalBest) {
            SSFile.personalBest = timeTakenToEnd
            personalBest = timeTakenToEnd
        }

        val isPB = if (timeTakenToEnd <= personalBest) "§3§l(PB)" else "§3($personalBest)"
        val message = "Took ${timeTakenToEnd}s to finish Simon Says. $isPB"
        if (config.timeInPartyChat && PartyAPI.inParty) {
            HypixelCommands.partyChat(message.cleanString())
        } else {
            ChatUtils.addMessage(message)
        }

        try {
            SSFile.save()
        } catch (ex: IOException) {
            NobaAddons.LOGGER.error("Failed to save new time/personal best", ex)
        }
    }

    private fun processOtherTime(username: String) {
        if (!config.timeOtherPlayers) return

        val timeTakenToEnd = (deviceEndTime.toMillis() - deviceStartTime.toMillis()).toDouble() / 1000

        val message = "$username took ${timeTakenToEnd}s to complete Simon Says."
        ChatUtils.addMessage(message)
    }

    private fun resetState() {
        deviceStartTime = Timestamp.distantPast()
        deviceEndTime = Timestamp.distantPast()
        firstButtonPressed = false
        deviceDone = false
        playerDoingDevice = null
    }

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (!isEnabled()) return
        if (!DungeonUtils.isInCatacombsFloor(7)) return
        if (deviceDone) return

        val receivedMessage = event.message.unformattedText.cleanString()

        deviceFinishPattern.matchMatcher(receivedMessage) {
            val username = group("username")
            deviceEndTime = Timestamp.currentTime()
            deviceDone = true

            if (playerDoingDevice == username)
                processPersonalTime() else processOtherTime(username)
        }
    }

    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!isEnabled()) return
        if (event.entityPlayer != mc.thePlayer) return
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return
        if (firstButtonPressed) return
        if (deviceDone) return

        val blockState = event.world.getBlockState(event.pos) ?: return
        if (blockState.block == null) return
        if (blockState.block !is BlockButtonStone) return

        firstButtonPressed = true
        deviceStartTime = Timestamp.currentTime()
        playerDoingDevice = Utils.getPlayerName()
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        if (!config.timeOtherPlayers) return

        val isActivate = mc.theWorld?.isBlockPowered(stoneButton) ?: return
        if (!isActivate) return

        if (deviceStartTime == Timestamp.distantPast()) deviceStartTime = Timestamp.currentTime()
        firstButtonPressed = true
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        resetState()
    }

    fun isEnabled() = config.enabled && SkyblockUtils.isInLocation(Location.CATACOMBS) && DungeonUtils.isInPhase(DungeonBoss.GOLDOR)
}