package me.nobaboy.nobaaddons.keybinds;

import me.nobaboy.nobaaddons.util.ChatUtils;
import org.lwjgl.input.Keyboard;

public class CommandKeyBind extends NobaKeyBind {
	public CommandKeyBind(String name, int key, String command) {
		super(name, key, () -> ChatUtils.sendCommand(command));
	}

	public CommandKeyBind(String name, String command) {
		this(name, Keyboard.KEY_NONE, command);
	}
}
