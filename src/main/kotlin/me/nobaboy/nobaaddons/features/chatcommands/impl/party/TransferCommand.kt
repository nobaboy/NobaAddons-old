package me.nobaboy.nobaaddons.features.chatcommands.impl.party

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.api.PartyAPI
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.StringUtils.lowercaseEquals
import me.nobaboy.nobaaddons.util.Utils

class TransferCommand : IChatCommand {
    override val name: String = "transfer"

    override val aliases: MutableList<String> = mutableListOf("ptme", "pt")

    override val usage: String = "(transfer|pt) [optional: username], ptme"

    override val isEnabled: Boolean
        get() = NobaAddons.config.partyTransferCommand

    override fun run(ctx: ChatContext) {
        if (PartyAPI.isLeader()) return
        if (ctx.user() == Utils.getPlayerName()) return

        if (!ctx.command().lowercaseEquals("ptme")) {
            val player = if (ctx.args().isEmpty()) ctx.user() else ctx.args()[0]
            ChatUtils.queueCommand("p transfer $player")
            return
        }

        ChatUtils.queueCommand("p transfer ${ctx.user()}")
    }
}