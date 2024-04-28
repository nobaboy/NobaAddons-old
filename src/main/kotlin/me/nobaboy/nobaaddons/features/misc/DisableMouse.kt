package me.nobaboy.nobaaddons.features.misc

import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.ChatUtils
import net.minecraft.client.settings.GameSettings
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object DisableMouse {
    private var disableMouse: Boolean = false
    private var sensitivity: Float = 0.5F

    fun toggleMouse() {
        disableMouse = !disableMouse
        val gameSettings: GameSettings = mc.gameSettings

        if (disableMouse) {
            sensitivity = gameSettings.mouseSensitivity
            gameSettings.mouseSensitivity = -1 / 3F
        } else {
            gameSettings.mouseSensitivity = sensitivity
            gameSettings.saveOptions()
        }

        val message = if (disableMouse) "Mouse movement disabled" else "Mouse movement enabled"
        ChatUtils.addMessage(message)
    }

    @SubscribeEvent
    fun onWorldUnload(ignored: WorldEvent.Unload) {
        if (!disableMouse) return

        mc.gameSettings.mouseSensitivity = sensitivity
        mc.gameSettings.saveOptions()
        disableMouse = false
    }
}