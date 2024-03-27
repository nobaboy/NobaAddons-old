package me.nobaboy.nobaaddons.commands;

import com.google.common.collect.Lists;
import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.dungeons.SSDeviceTimer;
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.TickDelay;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.List;

public class NobaCommand extends CommandBase {
    public String getHelpMessage(String type) {
        String message = "";
        switch(type) {
            case "help":
                message = "§7§m-----------------§r§7[ §9NobaAddons §7]§m-----------------\n" +
                        " §3§l◆ §3Mod Version: §b" + NobaAddons.MOD_VERSION + "\n\n" +
                        " §9§l➜ Commands and Info:\n" +
                        "   §3/nobaaddons (Alias: /noba) §7» §rOpens the mod Config GUI.\n" +
                        "   §3/noba help §7» §rSends this help message.\n" +
                        " §9§l➜ Chat Commands:\n" +
                        "   §3/noba dmCommands §7» §rSends usable DM Commands.\n" +
                        "   §3/noba partyCommands §7» §rSends usable Party Commands.\n" +
                        "   §3/noba guildCommands §7» §rSends usable Guild Commands.\n" +
                        " §9§l➜ Dungeons:\n" +
                        "   §3/noba ssPB §7» §rGet SS PB time.\n" +
                        "   §3/noba ssAverage §7» §rSend average time taken to complete SS Device.\n" +
                        "   §3/noba ssRemoveLast §7» §rRemove the last SS time.\n" +
                        "   §3/noba ssClearTimes §7» §rClear SS Times.\n" +
                        "§7§m-----------------------------------------------";
                break;
            case "dmcommands":
                message = "§7§m-----------------§r§7[ §9DM Commands §7]§m-----------------\n" +
                        " §3!warpme §7» §rWarp user to your lobby.\n" +
                        " §3!partyme (Alias: !pme) §7» §rInvite user to party.\n" +
                        "§7§m-----------------------------------------------";
                break;
            case "partycommands":
                message = "§7§m-----------------§r§7[ §9Party Commands §7]§m-----------------\n" +
                        " §3!help §7» §rSends all usable commands.\n" +
                        " §3!ptme (Alias: !transfer or !pt) §7» §rTransfer party to the player who ran the command.\n" +
                        " §3!allinvite (Alias: !allinv) §7» §rTurns on all invite party setting.\n" +
                        " §3!warp [optional: seconds] §7» §rRequests party warp with an optional warp delay.\n" +
                        " §3!cancel §7» §rStop the current delayed warp.\n" +
                        " §3!warpme §7» §rWarp user to your lobby.\n" +
                        " §3!coords §7» §rSends current location of user.\n" +
                        "§7§m--------------------------------------------------";
                break;
            case "guildcommands":
                message = "§7§m-----------------§r§7[ §9Guild Commands §7]§m-----------------\n" +
                        " §3!help §7» §rSends all usable commands.\n" +
                        " §3!warpout [username] §7» §rWarp out a player.\n" +
                        "§7§m-------------------------------------------------";
                break;
        }
        return message;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "nobaaddons";
    }

    @Override
    public List<String> getCommandAliases() {
        return Lists.newArrayList("noba");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Use '/noba help' to view all commands";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1) {
            new TickDelay(() -> Minecraft.getMinecraft().displayGuiScreen(NobaAddons.config.gui()), 1);
            return;
        }
        switch(args[0].toLowerCase()) {
            case "help":
                ChatUtils.addMessage(false, getHelpMessage("help"));
                break;
            case "dmcommands":
            case "guildcommands":
            case "partycommands":
                ChatUtils.addMessage(false, getHelpMessage(args[0].toLowerCase()));
                break;
            case "sspersonalbest":
            case "sspb":
                Double personalBest = SSFile.INSTANCE.personalBest.get();
                String message = (personalBest == null) ?
                        "You have not completed a single Simon Says device in the Catacombs Floor 7." :
                        "Your SS PB is: " + personalBest;
                ChatUtils.addMessage(message);
                break;
            case "ssaverage":
                SSDeviceTimer.sendAverage();
                break;
            case "ssremovelast":
                SSDeviceTimer.modifySSTimes(true);
                break;
            case "sscleartimes":
                SSDeviceTimer.modifySSTimes(false);
                break;
            default:
                ChatUtils.addMessage(true,  "This command doesn't exist, use '/noba help' to view all commands.");
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args,
                "help", "dmCommands", "partyCommands", "guildCommands", "ssPB", "ssAverage", "ssRemoveLast", "ssClearTimes"
        ) : null;
    }
}