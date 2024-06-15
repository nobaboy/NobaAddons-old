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
import net.minecraft.entity.player.EntityPlayer
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

    private val completionPattern: Pattern = Pattern.compile("^(?<username>[A-z0-9_]+) completed a device! \\([1-7]/7\\)")
    private val buttonPosition = BlockPos(110, 121, 91)

    private var startTime = Timestamp.distantPast()
    private var endTime = Timestamp.distantPast()
    private var buttonPressed: Boolean = false
    private var deviceCompleted: Boolean = false

    private fun processPersonalTime() {
        val times: MutableList<Double> = SSFile.times
        val duration = (endTime.toMillis() - startTime.toMillis()).toDouble() / 1000
        times.add(duration)

        val personalBest = SSFile.personalBest?.takeIf { duration >= it } ?: duration.also { SSFile.personalBest = it }
        val isPersonalBest = if (duration < personalBest) "§3§l(PB)" else "§3($personalBest)"
        val message = "Took ${duration}s to finish Simon Says. $isPersonalBest"

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

    private fun processOtherPlayerTime(username: String) {
        if (!config.timeOtherPlayers) return

        val duration = (endTime.toMillis() - startTime.toMillis()).toDouble() / 1000
        val message = "$username took ${duration}s to complete Simon Says."
        ChatUtils.addMessage(message)
    }

    private fun resetState() {
        startTime = Timestamp.distantPast()
        endTime = Timestamp.distantPast()
        buttonPressed = false
        deviceCompleted = false
    }

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (!isEnabled()) return
        if (!DungeonUtils.isInCatacombsFloor(7)) return
        if (!buttonPressed) return
        if (deviceCompleted) return

        val receivedMessage = event.message.unformattedText.cleanString()

        completionPattern.matchMatcher(receivedMessage){
            val username = group("username")
            val nearbyEntity = EntityUtils.getEntitiesNear<EntityPlayer>(buttonPosition.toNobaVec(), 5.0)
                .find { it.name.contains(username) }

            if (nearbyEntity == null) {
                resetState()
                return@matchMatcher
            }

            endTime = Timestamp.currentTime()
            deviceCompleted = true

            if (Utils.getPlayerName() in nearbyEntity.name) {
                processPersonalTime()
            } else {
                processOtherPlayerTime(username)
            }
        }
    }

    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!isEnabled()) return
        if (event.entityPlayer != mc.thePlayer) return
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return
        if (event.pos != buttonPosition) return
        if (buttonPressed) return
        if (deviceCompleted) return

        val blockState = event.world.getBlockState(event.pos) ?: return
        if (blockState.block !is BlockButtonStone) return

        buttonPressed = true
        startTime = Timestamp.currentTime()
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        if (!config.timeOtherPlayers) return

        val isButtonPowered = mc.theWorld?.isBlockPowered(buttonPosition) ?: return
        if (!isButtonPowered) return

        if (startTime == Timestamp.distantPast()) startTime = Timestamp.currentTime()
        buttonPressed = true
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        resetState()
    }

    fun isEnabled() = config.enabled && SkyblockUtils.isInLocation(Location.CATACOMBS) && DungeonUtils.isInPhase(DungeonBoss.GOLDOR)
}