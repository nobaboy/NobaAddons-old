package me.nobaboy.nobaaddons.config.features.dungeons

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorInfoText
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class SimonSaysConfig {
    @Expose
    @JvmField
    @ConfigOption(name = "Note", desc = "The times have a ±0.5s margin of error.")
    @ConfigEditorInfoText
    var marginWarning: Nothing? = null

    @Expose
    @JvmField
    @ConfigOption(name = "Enabled", desc = "Announces how much time it took to complete the Simon Says device.")
    @ConfigEditorBoolean
    var enabled = false

    @Expose
    @JvmField
    @ConfigOption(name = "Time in Party Chat", desc = "Instead of the message being client-side, send it in party chat.")
    @ConfigEditorBoolean
    var timeInPartyChat = false

    @Expose
    @JvmField
    @ConfigOption(name = "Time Other Players", desc = "Announces how much time it took for other players to complete the Simon Says Device.")
    @ConfigEditorBoolean
    var timeOtherPlayers = false
}