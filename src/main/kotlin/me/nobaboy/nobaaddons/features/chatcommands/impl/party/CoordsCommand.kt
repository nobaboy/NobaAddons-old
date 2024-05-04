package me.nobaboy.nobaaddons.features.chatcommands.impl.party

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand
import me.nobaboy.nobaaddons.util.HypixelCommands

class CoordsCommand : IChatCommand {
    override val name: String = "coords"

    override val isEnabled: Boolean
        get() = NobaAddons.config.partyCoordsCommand

    override fun run(ctx: ChatContext) {
        val player = mc.thePlayer
        val posX = player.posX.toInt()
        val posY = player.posY.toInt()
        val posZ = player.posZ.toInt()

        HypixelCommands.partyChat("x: $posX, y: $posY, z: $posZ")
    }
}