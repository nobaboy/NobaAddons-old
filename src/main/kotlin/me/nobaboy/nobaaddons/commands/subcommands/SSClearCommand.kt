package me.nobaboy.nobaaddons.commands.subcommands

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.commands.ISubCommand
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile
import me.nobaboy.nobaaddons.util.ChatUtils
import java.io.IOException

class SSClearCommand : ISubCommand {
    override val name: String = "ssClearTimes"

    override val aliases: List<String> = listOf("ssClear")

    override fun run(args: Array<String>) {
        val times = SSFile.times

        if (times.isEmpty()) {
            ChatUtils.addMessage("You have not completed a single Simon Says device in the Catacombs Floor 7.")
            return
        }

        try {
            ChatUtils.addMessage("Successfully cleared SS Times.")
            SSFile.personalBest = null
            SSFile.times.clear()
            SSFile.save()
        } catch (ex: IOException) {
            NobaAddons.LOGGER.error("Failed to modify simon-says-times.json", ex)
        }
    }
}