package me.nobaboy.nobaaddons.keybinds

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.CooldownManager
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

open class NobaKeyBind(
    name: String, key: Int = Keyboard.KEY_NONE, private val onPress: () -> Unit
) : KeyBinding(name, key, NobaAddons.MOD_NAME) {
    private val cooldown = CooldownManager()

    constructor(name: String, onPress: () -> Unit) : this(name, Keyboard.KEY_NONE, onPress)

    fun maybePress() {
        if (this.isPressed && !cooldown.isOnCooldown()) {
            cooldown.startCooldown(500L)
            onPress()
        }
    }
}