package me.nobaboy.nobaaddons.features.chatcommands.impl.dm

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.WarpPlayerHandler
import me.nobaboy.nobaaddons.util.HypixelCommands
import me.nobaboy.nobaaddons.util.Utils

class WarpUserCommand : IChatCommand {
    override val name: String = "warpme"

    override val isEnabled: Boolean
        get() = NobaAddons.config.chatCommands.dmCommands.warpMe

    override fun run(ctx: ChatContext) {
        val playerName = ctx.user()
        if (playerName == Utils.getPlayerName()) return

        if (WarpPlayerHandler.isWarping) {
            HypixelCommands.privateChat(playerName, "Warp-in is on cooldown, try again later!")
            return
        }

        WarpPlayerHandler.warpPlayer(playerName, false, "msg $playerName")
    }
}