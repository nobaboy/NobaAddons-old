package me.nobaboy.nobaaddons.features.notifiers.misc

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.LocationUtils
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class CenturyCakes {
    private val config get() = NobaAddons.config.notifiers.centuryCakesNotifier
    private var cakesEaten = 0

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (!isEnabled()) return

        val receivedMessage = event.message.unformattedText.cleanString()

        if (receivedMessage.startsWith("Yum! You gain")) {
            if (cakesEaten++ >= config.cakesAmount) {
                ChatUtils.delayedAdd("All cakes eaten!")
                mc.thePlayer.playSound("note.pling", 1F, 2.0F)
                cakesEaten = 0
            }
        }
    }

    fun isEnabled() = config.enabled && LocationUtils.inSkyblock
}