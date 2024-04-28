package me.nobaboy.nobaaddons.commands

interface ISubCommand {
    val name: String

    val aliases: List<String>
        get() = emptyList()

    val isEnabled: Boolean
        get() = true

    fun run(args: Array<String>)
}