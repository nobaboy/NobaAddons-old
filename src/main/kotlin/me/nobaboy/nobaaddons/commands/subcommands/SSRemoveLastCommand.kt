package me.nobaboy.nobaaddons.commands.subcommands

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.commands.ISubCommand
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile
import me.nobaboy.nobaaddons.util.ChatUtils
import java.io.IOException

class SSRemoveLastCommand : ISubCommand {
    override val name: String = "ssRemoveLast"

    override fun run(args: Array<String>) {
        val times = SSFile.times
        val personalBest = SSFile.personalBest

        if (times.isEmpty()) {
            ChatUtils.addMessage("You have not completed a single Simon Says device in the Catacombs Floor 7.")
            return
        }

        try {
            if (times.size > 1) {
                ChatUtils.addMessage("Successfully removed last SS Time.")
                val isPB = times.last() == personalBest
                times.removeAt(times.lastIndex)
                if (isPB) {
                    val newPB = times.min()
                    SSFile.personalBest = newPB
                }
            } else {
                ChatUtils.addMessage("Successfully cleared SS Times.")
                SSFile.personalBest = null
                SSFile.times.clear()
            }
            SSFile.save()
        } catch (ex: IOException) {
            NobaAddons.LOGGER.error("Failed to modify simon-says-times.json", ex)
        }
    }
}