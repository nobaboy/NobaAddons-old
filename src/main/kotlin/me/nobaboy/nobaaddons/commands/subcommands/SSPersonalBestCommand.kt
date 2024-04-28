package me.nobaboy.nobaaddons.commands.subcommands

import me.nobaboy.nobaaddons.commands.ISubCommand
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile
import me.nobaboy.nobaaddons.util.ChatUtils

class SSPersonalBestCommand : ISubCommand {
    override val name: String = "ssPersonalBest"

    override val aliases: List<String> = listOf("ssPB")

    override fun run(args: Array<String>) {
        val personalBest = SSFile.personalBest
        val message = personalBest?.let {
            "Your SS PB is: $personalBest"
        } ?: "You have not completed a single Simon Says device in the Catacombs Floor 7."
        ChatUtils.addMessage(message)
    }
}