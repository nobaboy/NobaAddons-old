package me.nobaboy.nobaaddons.keybinds

import me.nobaboy.nobaaddons.util.ChatUtils
import org.lwjgl.input.Keyboard

class CommandKeyBind(name: String, key: Int = Keyboard.KEY_NONE, command: String) : NobaKeyBind(
    name, key, { ChatUtils.queueCommand(command) }
) {
    constructor(name: String, command: String) : this(name, Keyboard.KEY_NONE, command)
}