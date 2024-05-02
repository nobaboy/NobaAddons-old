package me.nobaboy.nobaaddons.features.chatcommands.impl

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.features.chatcommands.ChatCommandManager
import me.nobaboy.nobaaddons.features.chatcommands.impl.dm.PartyMeCommand
import me.nobaboy.nobaaddons.features.chatcommands.impl.dm.WarpUserCommand
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.HelpCommand
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.WarpOutCommand
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
        register(HelpCommand(this, "r", NobaAddons.config.dmHelpCommand))
        register(WarpOutCommand("r", NobaAddons.config.dmWarpOutCommand))
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

        if (WarpUserCommand.isWarpingUser) {
            if (receivedMessage.lowercaseContains("${WarpUserCommand.player} is already in the party")) {
                WarpUserCommand.isWarpingUser = false
                return
            } else if (receivedMessage.lowercaseContains("${WarpUserCommand.player} joined the party")) {
                WarpUserCommand.playerJoined = true
                return
            }
        }

        if (WarpOutCommand.isWarpingOut) {
            if (receivedMessage.lowercaseContains("${WarpOutCommand.player} is already in the party")) {
                WarpOutCommand.isWarpingOut = false
                return
            } else if (receivedMessage.lowercaseContains("${WarpOutCommand.player} joined the party")) {
                WarpOutCommand.playerJoined = true
                return
            }
        }

        processMessage(receivedMessage)
    }

    fun isEnabled() = NobaAddons.config.dmCommands
}