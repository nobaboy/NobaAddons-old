package me.nobaboy.nobaaddons.core

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class NotifiersConfig {
	@Expose
	@JvmField
	@ConfigOption(name = "Cakes Eaten Notifier", desc = "Notifies you once you've eaten all Century Cakes.")
	@ConfigEditorBoolean
	var cakesEatenNotifier = false

	@Expose
	@JvmField
	@ConfigOption(name = "Current Century Cake Count", desc = "The current number of total Century Cakes that exist")
	@ConfigEditorSlider(minValue = 1f, maxValue = 100f, minStep = 1f)
	var centuryCakesAmount = 14

	@Expose
	@JvmField
	@ConfigOption(name = "Totem of Corruption", desc = "Notifies you when a placed Totem of Corruption expires.")
	@ConfigEditorBoolean
	var corruptionTotemNotifier = false
}
