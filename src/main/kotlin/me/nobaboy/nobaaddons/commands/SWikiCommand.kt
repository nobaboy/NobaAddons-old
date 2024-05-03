package me.nobaboy.nobaaddons.commands

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.StringUtils.capitalizeFirstLetters
import me.nobaboy.nobaaddons.util.Utils
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

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

        val wikiName = "§3§lOfficial SkyBlock Wiki§b"
        val hypixelWikiLink = "https://wiki.hypixel.net/index.php?search=${args.joinToString("+")}&scope=internal"

        // If auto-open on, just open
        if (NobaAddons.config.autoOpenSWiki) {
            ChatUtils.addMessage("Opening the $wikiName with search query '${args.joinToString(" ")}'.")
            Utils.openBrowser(hypixelWikiLink)
            return
        }

        // Provide the link otherwise
        ChatUtils.chatLink(
            "Click §3§lHERE §bto find '${args.joinToString(" ").capitalizeFirstLetters()}' on the $wikiName.",
        hypixelWikiLink,
        "§7View '${args.joinToString( )}' on the Official SkyBlock Wiki"
        )
    }
}