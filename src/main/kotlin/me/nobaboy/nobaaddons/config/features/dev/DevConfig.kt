package me.nobaboy.nobaaddons.config.features.dev

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class DevConfig {
    @Expose
    @JvmField
    @ConfigOption(name = "Debug Mode", desc = "Enables debug functionality. §cOnly turn this on if you were explicitly asked to.")
    @ConfigEditorBoolean
    var debugMode = false

    @Expose
    @JvmField
    @ConfigOption(name = "Remove Slash from Messages", desc = "Removes the slash from command messages sent by the mod. §cOnly turn this on if you know what you're doing.")
    @ConfigEditorBoolean
    var removeSlash = false
}