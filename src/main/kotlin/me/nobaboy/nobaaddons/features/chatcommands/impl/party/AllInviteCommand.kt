package me.nobaboy.nobaaddons.features.chatcommands.impl.party

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.api.PartyAPI
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand
import me.nobaboy.nobaaddons.util.ChatUtils

class AllInviteCommand : IChatCommand {
    override val name: String = "allinvite"

    override val aliases: MutableList<String> = mutableListOf("allinv")

    override val usage: String = "(allinvite|allinv)"

    override val isEnabled: Boolean
        get() = NobaAddons.config.partyAllInviteCommand

    override fun run(ctx: ChatContext) {
        if (PartyAPI.isLeader()) return
        ChatUtils.queueCommand("p settings allinvite")
    }
}