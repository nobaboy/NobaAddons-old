package me.nobaboy.nobaaddons.features.notifiers.dungeons

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.LocationUtils
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.StringUtils.lowercaseEquals
import me.nobaboy.nobaaddons.util.data.Location
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class CampBlood {
    private var timer: Job? = null

    private fun startTimer() {
        timer = NobaAddons.coroutineScope.launch {
            delay(NobaAddons.config.dungeons.timeTillBloodCamp.seconds)

            ChatUtils.addMessage("Go blood camp!")

            for (i in 1..10) {
                mc.thePlayer.playSound("note.pling", 1F, 2.0F)
                delay(100.milliseconds)
            }
        }
    }

    @SubscribeEvent
    fun onWorldUnload(ignored: WorldEvent.Unload) {
        timer?.cancel()
    }

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (!isEnabled()) return

        val receivedMessage = event.message.unformattedText.cleanString()
        if (receivedMessage.lowercaseEquals("The BLOOD DOOR has been opened!")) startTimer()
    }

    fun isEnabled() = NobaAddons.config.dungeons.bloodCampAfterTime && LocationUtils.isInLocation(Location.CATACOMBS)
}