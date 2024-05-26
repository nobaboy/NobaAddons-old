package me.nobaboy.nobaaddons.features.chat

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.StringUtils.matchMatcher
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Pattern

class HideTipMessages {
    private val alreadyTippedPattern: Pattern = Pattern.compile("You've already tipped someone in the past hour in [A-z ]+! Wait a bit and try again!")

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        val receivedMessage = event.message.unformattedText.cleanString()

        alreadyTippedPattern.matchMatcher(receivedMessage) {
            event.isCanceled
        }

        if (receivedMessage.startsWith("You tipped") ||
            receivedMessage.startsWith("You were tipped") ||
            receivedMessage == "That player is not online, try another user!" ||
            receivedMessage == "No one has a network booster active right now! Try again later." ||
            receivedMessage == "You already tipped everyone that has boosters active, so there isn't anybody to be tipped right now!" ||
            receivedMessage == "Slow down! You can only use /tip every few seconds."
        ) {
            event.isCanceled = true
        }
    }

    fun isEnabled() = NobaAddons.config.chat.hideTipMessages
}