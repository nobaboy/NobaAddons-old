package me.nobaboy.nobaaddons.features.chatcommands.impl

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.features.chatcommands.ChatCommandManager
import me.nobaboy.nobaaddons.features.chatcommands.impl.party.*
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.HelpCommand
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.RegexUtils.matchMatcher
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Matcher
import java.util.regex.Pattern

class PartyCommands : ChatCommandManager() {
    private val config get() = NobaAddons.config.chatCommands.partyCommands
    private val chatPattern: Pattern =
        Pattern.compile("^Party > .*?(?:\\[[A-Z+]+] )?(?<username>[A-z0-9_]+).*?: [!?.](?<command>[A-z0-9_]+) ?(?<argument>[A-z0-9_]+)?")

    init {
        register(HelpCommand(this, "pc", config::help))
        register(TransferCommand())
        register(AllInviteCommand())
        register(WarpCommand())
        register(CancelCommand())
        register(CoordsCommand())
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
        processMessage(receivedMessage)
    }

    fun isEnabled() = config.enabled
}