package me.nobaboy.nobaaddons.features.chatcommands.impl.dm

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand
import me.nobaboy.nobaaddons.util.HypixelCommands

class PartyMeCommand : IChatCommand {
    override val name: String = "partyme"

    override val aliases: MutableList<String> = mutableListOf("pme")

    override val isEnabled: Boolean
        get() = NobaAddons.config.dmPartyMeCommand

    override fun run(ctx: ChatContext) {
        HypixelCommands.partyInvite(ctx.user())
    }
}