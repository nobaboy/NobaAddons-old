package me.nobaboy.nobaaddons.commands;

import me.nobaboy.nobaaddons.util.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

import static net.minecraft.util.EnumChatFormatting.DARK_AQUA;

public class SWikiCommand extends CommandBase {
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "swiki";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/swiki <search query> - Allows you to search the Official Hypixel Wiki with any search query (if it exists)";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1) {
            ChatUtils.addMessage(DARK_AQUA + "Missing search query.");
            return;
        }
        ChatUtils.addMessage("Opening wiki.hypixel.net with search query '" + String.join(" ", args) + "'.");
        try {
            Desktop.getDesktop().browse(URI.create("https://wiki.hypixel.net/index.php?search=" + String.join("+", args) + "&scope=internal"));
        } catch(IOException ignored) { }
    }
}
