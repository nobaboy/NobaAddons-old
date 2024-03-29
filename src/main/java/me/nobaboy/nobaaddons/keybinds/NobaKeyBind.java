package me.nobaboy.nobaaddons.keybinds;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.CooldownManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class NobaKeyBind extends KeyBinding {
	private final CooldownManager cooldown = new CooldownManager();
	private final Runnable onPress;

	public NobaKeyBind(String name, int key, Runnable onPress) {
		super(name, key, NobaAddons.MOD_NAME);
		this.onPress = onPress;
	}

	public NobaKeyBind(String name, Runnable onPress) {
		this(name, Keyboard.KEY_NONE, onPress);
	}

	public void maybePress() {
		if(this.isPressed() && !cooldown.isOnCooldown()) {
			cooldown.startCooldown(500L);
			onPress.run();
		}
	}
}
