package me.nobaboy.nobaaddons.commands.subcommands

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.commands.ISubCommand
import me.nobaboy.nobaaddons.features.dungeons.PearlRefill

class RefillPearlsCommand : ISubCommand {
    override val name: String = "refillPearls"

    override val isEnabled: Boolean
        get() = NobaAddons.config.refillPearls

    override fun run(args: Array<String>) {
        PearlRefill.refillPearls(true)
    }
}