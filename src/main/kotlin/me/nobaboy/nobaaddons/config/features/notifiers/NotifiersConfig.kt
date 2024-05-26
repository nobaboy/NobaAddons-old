package me.nobaboy.nobaaddons.config.features.notifiers

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Accordion
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class NotifiersConfig {
	@Expose
	@JvmField
	@ConfigOption(name = "Century Cakes Notifier", desc = "")
	@Accordion
	var centuryCakesNotifier = CenturyCakesConfig()

	@Expose
	@JvmField
	@ConfigOption(name = "Totem of Corruption Notifier", desc = "Notifies you when your placed Totem of Corruption expires.")
	@ConfigEditorBoolean
	var corruptionTotemNotifier = false
}
