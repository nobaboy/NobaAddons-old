package me.nobaboy.nobaaddons.commands

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.commands.subcommands.*
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.StringUtils.lowercaseEquals
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import java.util.*

class NobaCommand : CommandBase() {
    private val subCommands = mutableListOf<ISubCommand>()

    init {
        subCommands.add(HelpCommand())
        subCommands.add(RefillPearlsCommand())
        subCommands.add(SSAverageCommand())
        subCommands.add(SSClearCommand())
        subCommands.add(SSPersonalBestCommand())
        subCommands.add(SSRemoveLastCommand())
        subCommands.add(DebugPartyCommand())
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun getCommandName(): String {
        return "nobaaddons"
    }

    override fun getCommandAliases(): MutableList<String> {
        return mutableListOf("noba")
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "Use '/noba help' to view all usable commands"
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            NobaAddons.openConfigGui = true
            return
        }

        val commandName = args[0]
        val command = subCommands.firstOrNull { cmd ->
            cmd.name lowercaseEquals commandName ||
            cmd.aliases.any { alias -> alias lowercaseEquals commandName }
        }

        if (command == null) {
            ChatUtils.addMessage("This command doesn't exist, use '/noba help' to view all commands.")
            return
        }

        command.run(Arrays.copyOfRange(args, 1, args.size))
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender,
        args: Array<out String>,
        pos: BlockPos
    ): List<String>? {
        return when (args.size) {
            1 -> {
                val commands = subCommands.filter { it.isEnabled }
                    .map { mutableListOf(it.name).apply { addAll(it.aliases) } }.flatten()
                getListOfStringsMatchingLastWord(args, commands)
            }

            2 -> {
                if (args[0].lowercaseEquals("help")) {
                    getListOfStringsMatchingLastWord(args, "dmCommands", "partyCommands", "guildCommands")
                } else {
                    emptyList()
                }
            }

            else -> emptyList()
        }
    }
}