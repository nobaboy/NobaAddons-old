package me.nobaboy.nobaaddons.features.chatcommands.impl

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.features.chatcommands.ChatCommandManager
import me.nobaboy.nobaaddons.features.chatcommands.impl.dm.PartyMeCommand
import me.nobaboy.nobaaddons.features.chatcommands.impl.dm.WarpUserCommand
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.HelpCommand
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.WarpOutCommand
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.WarpPlayerHandler
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.StringUtils.lowercaseContains
import me.nobaboy.nobaaddons.util.StringUtils.matchMatcher
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Matcher
import java.util.regex.Pattern

class DMCommands : ChatCommandManager() {
    private val chatPattern: Pattern =
        Pattern.compile("^From (?:\\[[A-Z+]+] )?(?<username>[A-z0-9_]+): [!?.](?<command>[A-z0-9_]+) ?(?<argument>[A-z0-9_]+)?")

    init {
        register(HelpCommand(this, "msg", NobaAddons.config.dmHelpCommand))
        register(WarpOutCommand("msg", NobaAddons.config.dmWarpOutCommand))
        register(WarpUserCommand())
        register(PartyMeCommand())
    }

    override fun matchMessage(message: String): Matcher? {
        chatPattern.matchMatcher(message) {
            return this
        }
        return null
    }

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (!isEnabled()) return

        val receivedMessage = event.message.unformattedText.cleanString()

        if (WarpPlayerHandler.isWarping) {
            val playerName = WarpPlayerHandler.player
            if (receivedMessage.lowercaseContains("$playerName is already in the party")) {
                WarpPlayerHandler.reset(true)
                return
            } else if (receivedMessage.lowercaseContains("$playerName joined the party")) {
                WarpPlayerHandler.playerJoined = true
                return
            }
        }

        processMessage(receivedMessage)
    }

    fun isEnabled() = NobaAddons.config.dmCommands
}