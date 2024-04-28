package me.nobaboy.nobaaddons.features.chatcommands

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.CooldownManager
import me.nobaboy.nobaaddons.util.StringUtils.lowercaseEquals
import java.util.regex.Matcher

abstract class ChatCommandManager : CooldownManager() {
    private val commands = mutableListOf<IChatCommand>()
    private val lock = Object()

    protected fun register(command: IChatCommand) {
        commands.add(command)
    }

    fun getCommands(): List<IChatCommand> {
        return commands
    }

    protected abstract fun matchMessage(message: String): Matcher?

    private fun getContext(message: String): ChatContext? {
        val match = matchMessage(message) ?: return null
        val user = match.group("username")
        val command = match.group("command")
        val args = match.group("argument")?.split(" ")?.toList() ?: emptyList()
        return ChatContext(user, command, args, message)
    }

    fun processMessage(message: String) {
        synchronized(lock) {
            val ctx = getContext(message) ?: return
            val cmd = commands.asSequence()
                .filter { it.isEnabled }
                .firstOrNull {
                    it.name.lowercaseEquals(ctx.command()) ||
                    it.aliases.any { alias ->
                        alias.lowercaseEquals(ctx.command())
                    }
                } ?: return

            if (!cmd.bypassCooldown && isOnCooldown()) return
            try {
                cmd.run(ctx)
                if (!NobaAddons.config.debugMode && !cmd.bypassCooldown) startCooldown()
            } catch(ex: Exception) {
                NobaAddons.LOGGER.error("Failed to run chat command '$cmd' | Name: '${cmd.name}`.", ex)
            }
        }
    }
}