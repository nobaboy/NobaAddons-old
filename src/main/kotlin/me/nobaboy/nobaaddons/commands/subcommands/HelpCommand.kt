package me.nobaboy.nobaaddons.commands.subcommands

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.commands.ISubCommand
import me.nobaboy.nobaaddons.util.ChatUtils

class HelpCommand : ISubCommand {
    fun getHelpMessage(type: String): String {
        return when (type.lowercase()) {
            "dmcommands" ->
                """
                    #§7§m-----------------§r§7[ §9DM Commands §7]§m-----------------
                    # §3Prefixes: §r! ? . 
                    #
                    # §3help §7» §rSends all usable commands.
                    # §3warpme §7» §rWarp user to your lobby.
                    # §3partyme (Alias: !pme) §7» §rInvite user to party.
                    #§7§m-----------------------------------------------
                """.trimMargin("#")

            "partycommands" ->
                """
                    #§7§m-----------------§r§7[ §9Party Commands §7]§m-----------------
                    # §3Prefixes: §r! ? . 
                    #
                    # §3help §7» §rSends all usable commands.
                    # §3ptme (Alias: !transfer or !pt) §7» §rTransfer party to the player who ran the command."
                    # §3allinvite (Alias: !allinv) §7» §rTurns on all invite party setting.
                    # §3warp [optional: seconds] §7» §rRequests party warp with an optional warp delay.
                    # §3cancel §7» §rStop the current delayed warp.
                    # §3warpme §7» §rWarp user to your lobby.
                    # §3coords §7» §rSends current location of user.
                    #§7§m--------------------------------------------------
                """.trimMargin("#")

            "guildcommands" ->
                """
                    #§7§m-----------------§r§7[ §9Guild Commands §7]§m-----------------
                    # §3Prefixes: §r! ? . 
                    #
                    # §3help §7» §rSends all usable commands.
                    # §3warpout [username] §7» §rWarp out a player.
                    #§7§m-------------------------------------------------
                """.trimMargin("#")

            else ->
                """
                    #§7§m-----------------§r§7[ §9NobaAddons §7]§m-----------------
                    # §3§l◆ §3Mod Version: §b${NobaAddons.MOD_VERSION}
                    #
                    # §9§l➜ Commands and Info:
                    #   §3/nobaaddons (Alias: /noba) §7» §rOpens the mod Config GUI.
                    #   §3/noba help §7» §rSends this help message.
                    # §9§l➜ Chat Commands:
                    #   §3/noba help dmCommands §7» §rSends usable DM Commands.
                    #   §3/noba help partyCommands §7» §rSends usable Party Commands.
                    #   §3/noba help guildCommands §7» §rSends usable Guild Commands.
                    # §9§l➜ Dungeons:
                    #   §3/noba ssPB §7» §rGet SS PB time.
                    #   §3/noba ssAverage §7» §rSend average time taken to complete SS Device.
                    #   §3/noba ssRemoveLast §7» §rRemove the last SS time.
                    #   §3/noba ssClearTimes §7» §rClear SS Times.
                    #   §3/noba refillPearls §7» §rRefills you with pearls up to 16.
                    # §9§l➜ Debug:
                    #   §3/noba debugParty §7» Sends a debug response of the users party
                    #§7§m-----------------------------------------------
                """.trimMargin("#")
        }
    }

    override val name: String = "help"

    override fun run(args: Array<String>) {
        val helpMessage = getHelpMessage((if (args.isNotEmpty()) args[0] else ""))
        ChatUtils.addMessage(false, helpMessage)
    }
}