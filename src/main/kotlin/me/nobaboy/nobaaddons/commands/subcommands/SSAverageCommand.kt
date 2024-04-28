package me.nobaboy.nobaaddons.commands.subcommands

import me.nobaboy.nobaaddons.commands.ISubCommand
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile
import me.nobaboy.nobaaddons.util.ChatUtils

class SSAverageCommand : ISubCommand {
    override val name: String = "ssAverage"

    override val aliases: List<String> = listOf("ssAvg")

    override fun run(args: Array<String>) {
        val times = SSFile.times
        val count = times.size

        if (count == 0) {
            ChatUtils.addMessage("You have not completed a single Simon Says device in the Catacombs Floor 7.")
            return
        }

        val sum = times.sum()
        val average = sum / count

        val formattedAverage = "%.3f".format(average)
        ChatUtils.addMessage("Your average time for Simon Says is: ${formattedAverage}s (Total SS Devices: $count)")
    }
}