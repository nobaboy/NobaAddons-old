package me.nobaboy.nobaaddons.commands

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.ChatUtils
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import java.awt.Desktop
import java.io.IOException
import java.net.URI

class SWikiCommand : CommandBase() {
    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun getCommandName(): String {
        return "swiki"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/swiki <search query> - Allows you to search the Official Hypixel Wiki with any search query (if it exists)"
    }

    override fun processCommand(sender: ICommandSender?, args: Array<String>) {
        if (args.isEmpty()) {
            ChatUtils.addMessage("§3Missing search query.")
            return
        }
        ChatUtils.addMessage("Opening wiki.hypixel.net with search query '${args.joinToString(" ")}'.")
        try {
            Desktop.getDesktop()
                .browse(URI.create("https://wiki.hypixel.net/index.php?search=${args.joinToString("+")}&scope=internal"))
        } catch (ex: IOException) {
            NobaAddons.LOGGER.error("Failed to open hypixel wiki, search query: '$args'", ex)
        }
    }
}