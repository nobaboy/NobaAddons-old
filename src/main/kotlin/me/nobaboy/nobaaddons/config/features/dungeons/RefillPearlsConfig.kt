package me.nobaboy.nobaaddons.config.features.dungeons

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class RefillPearlsConfig {
    @Expose
    @JvmField
    @ConfigOption(name = "Enabled", desc = "Enables §b/noba refillPearls§r and the associated keybind, grabbing ender pearls out of your sacks when used.")
    @ConfigEditorBoolean
    var enabled = false

    @Expose
    @JvmField
    @ConfigOption(name = "Auto Refill Pearls", desc = "Automatically refill your pearls on dungeon start instead of pressing a key bind or running a command. §cUse at your own risk!")
    @ConfigEditorBoolean
    var autoRefill = false
}