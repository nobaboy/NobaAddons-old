package me.nobaboy.nobaaddons.config.features.chat.chatfilter.general

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class GeneralChatFilterConfig {
    @Expose
    @JvmField
    @ConfigOption(name = "Tip Messages", desc = "Toggles the messages sent when you tip a player or are tipped by someone else.")
    @ConfigEditorBoolean
    var hideTipMessages = false
}