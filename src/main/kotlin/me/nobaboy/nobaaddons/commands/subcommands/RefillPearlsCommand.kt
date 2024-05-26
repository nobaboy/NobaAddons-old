package me.nobaboy.nobaaddons.commands.subcommands

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.commands.ISubCommand
import me.nobaboy.nobaaddons.features.misc.PearlRefill

class RefillPearlsCommand : ISubCommand {
    override val name: String = "refillPearls"

    override val isEnabled: Boolean
        get() = NobaAddons.config.dungeons.refillPearls

    override fun run(args: Array<String>) {
        PearlRefill.refillPearls(true)
    }
}