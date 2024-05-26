package me.nobaboy.nobaaddons.config.features.dungeons

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class SimonSaysConfig {
    @Expose
    @JvmField
    @ConfigOption(name = "Enabled", desc = "Announces how much time it took to complete the Simon Says device. §8(±0.5 seconds error margin)")
    @ConfigEditorBoolean
    var enabled = false

    @Expose
    @JvmField
    @ConfigOption(name = "Time in Party Chat", desc = "Instead of the message being client-side, send it in party chat.")
    @ConfigEditorBoolean
    var timeInPartyChat = false
}