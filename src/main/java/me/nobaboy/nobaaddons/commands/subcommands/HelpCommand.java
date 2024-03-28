package me.nobaboy.nobaaddons.commands.subcommands;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.commands.ISubCommand;
import me.nobaboy.nobaaddons.util.ChatUtils;

public class HelpCommand implements ISubCommand {
	public String getHelpMessage(String type) {
		switch(type.toLowerCase()) {
			case "dmcommands":
				return "§7§m-----------------§r§7[ §9DM Commands §7]§m-----------------\n" +
						" §3!warpme §7» §rWarp user to your lobby.\n" +
						" §3!partyme (Alias: !pme) §7» §rInvite user to party.\n" +
						"§7§m-----------------------------------------------";
			case "partycommands":
				return "§7§m-----------------§r§7[ §9Party Commands §7]§m-----------------\n" +
						" §3!help §7» §rSends all usable commands.\n" +
						" §3!ptme (Alias: !transfer or !pt) §7» §rTransfer party to the player who ran the command.\n" +
						" §3!allinvite (Alias: !allinv) §7» §rTurns on all invite party setting.\n" +
						" §3!warp [optional: seconds] §7» §rRequests party warp with an optional warp delay.\n" +
						" §3!cancel §7» §rStop the current delayed warp.\n" +
						" §3!warpme §7» §rWarp user to your lobby.\n" +
						" §3!coords §7» §rSends current location of user.\n" +
						"§7§m--------------------------------------------------";
			case "guildcommands":
				return "§7§m-----------------§r§7[ §9Guild Commands §7]§m-----------------\n" +
						" §3!help §7» §rSends all usable commands.\n" +
						" §3!warpout [username] §7» §rWarp out a player.\n" +
						"§7§m-------------------------------------------------";
			default:
				return "§7§m-----------------§r§7[ §9NobaAddons §7]§m-----------------\n" +
						" §3§l◆ §3Mod Version: §b" + NobaAddons.MOD_VERSION + "\n\n" +
						" §9§l➜ Commands and Info:\n" +
						"   §3/nobaaddons (Alias: /noba) §7» §rOpens the mod Config GUI.\n" +
						"   §3/noba help §7» §rSends this help message.\n" +
						" §9§l➜ Chat Commands:\n" +
						"   §3/noba help dmCommands §7» §rSends usable DM Commands.\n" +
						"   §3/noba help partyCommands §7» §rSends usable Party Commands.\n" +
						"   §3/noba help guildCommands §7» §rSends usable Guild Commands.\n" +
						" §9§l➜ Dungeons:\n" +
						"   §3/noba ssPB §7» §rGet SS PB time.\n" +
						"   §3/noba ssAverage §7» §rSend average time taken to complete SS Device.\n" +
						"   §3/noba ssRemoveLast §7» §rRemove the last SS time.\n" +
						"   §3/noba ssClearTimes §7» §rClear SS Times.\n" +
						"§7§m-----------------------------------------------";
		}
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public void run(String[] args) {
		ChatUtils.addMessage(false, getHelpMessage(args.length > 0 ? args[0] : ""));
	}
}
