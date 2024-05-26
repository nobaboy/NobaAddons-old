package me.nobaboy.nobaaddons.config.features.chat

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class ChatConfig {
	@Expose
	@JvmField
	@ConfigOption(name = "Hide Tip Messages", desc = "Hides the messages sent when you tip a player or are tipped by someone else.")
	@ConfigEditorBoolean
	var hideTipMessages = false
}
