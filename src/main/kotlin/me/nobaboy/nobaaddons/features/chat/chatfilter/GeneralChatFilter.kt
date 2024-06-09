package me.nobaboy.nobaaddons.features.chat.chatfilter

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.RegexUtils.matches
import net.minecraftforge.client.event.ClientChatReceivedEvent
import java.util.regex.Pattern

object GeneralChatFilter {
    private val config get() = NobaAddons.config.chat.chatFilter.general

    private val alreadyTippedPattern: Pattern = Pattern.compile("You've already tipped someone in the past hour in [A-z ]+! Wait a bit and try again!")
    private val tipMessages = setOf(
        "That player is not online, try another user!",
        "No one has a network booster active right now, Try again later.",
        "You already tipped everyone that has boosters active, so there isn't anybody to be tipped right now!",
        "Slow down! You can only use /tip every few seconds."
    )

    fun processFilters(event: ClientChatReceivedEvent, message: String) {
        handleTipMessages(event, message)
    }

    private fun handleTipMessages(event: ClientChatReceivedEvent, message: String) {
        if (!config.hideTipMessages) return

        if (alreadyTippedPattern.matches(message) ||
            message.startsWith("You tipped") ||
            message.startsWith("You were tipped") ||
            tipMessages.any { message == it }) {
            event.isCanceled = true
        }
    }
}