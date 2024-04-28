package me.nobaboy.nobaaddons.features.chatcommands.impl.shared

import me.nobaboy.nobaaddons.features.chatcommands.ChatCommandManager
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand
import me.nobaboy.nobaaddons.util.ChatUtils

class HelpCommand(
    private val manager: ChatCommandManager,
    private val command: String,
    private val enabled: Boolean
) : IChatCommand {

    override val name: String = "help"

    override val isEnabled: Boolean
        get() = enabled

    override fun run(ctx: ChatContext) {
        val commands = manager.getCommands().map { it.usage }
        val commandsList = commands.joinToString(", ")

        ChatUtils.queueCommand("$command NobaAddons > [! ? .] | $commandsList")
    }
}