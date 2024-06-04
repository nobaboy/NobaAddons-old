package me.nobaboy.nobaaddons.features.notifiers.dungeons

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.SkyblockUtils
import me.nobaboy.nobaaddons.util.SoundUtils
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.StringUtils.lowercaseEquals
import me.nobaboy.nobaaddons.util.data.Location
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.seconds

class CampBlood {
    private val config get() = NobaAddons.config.dungeons.bloodCampTimer
    private var timer: Job? = null

    private fun startTimer() {
        timer = NobaAddons.coroutineScope.launch {
            delay(config.timeTillWarning.seconds)

            ChatUtils.addMessage("Go blood camp!")
            SoundUtils.repeatSound(100L, 10, "note.pling", 2.0F)
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

    fun isEnabled() = config.enabled && SkyblockUtils.isInLocation(Location.CATACOMBS)
}