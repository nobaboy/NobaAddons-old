package me.nobaboy.nobaaddons.features.notifiers.misc

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.StringUtils.cleanMessage
import me.nobaboy.nobaaddons.util.Utils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class CenturyCakes {
    private var cakesEaten = 0

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (!isEnabled()) return

        val receivedMessage = event.message.unformattedText.cleanMessage()

        if (receivedMessage.startsWith("Yum! You gain")) {
            if (cakesEaten++ >= NobaAddons.config.centuryCakesAmount) {
                ChatUtils.delayedAdd("All cakes eaten!")
                mc.thePlayer.playSound("note.pling", 1F, 2.0F)
                cakesEaten = 0
            }
        }
    }

    fun isEnabled() = NobaAddons.config.cakesEatenNotifier && Utils.inSkyblock
}