package me.nobaboy.nobaaddons.commands.subcommands

import me.nobaboy.nobaaddons.api.PartyAPI
import me.nobaboy.nobaaddons.commands.ISubCommand

class DebugPartyCommand : ISubCommand {
    override val name: String = "debugParty"

    override fun run(args: Array<String>) {
        PartyAPI.listMembers()
    }
}