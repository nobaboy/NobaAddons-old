package me.nobaboy.nobaaddons.core

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Category
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class DungeonsConfig {
	@Expose
	@JvmField
	@ConfigOption(name = "Camp Blood After Time", desc = "A timer that tell you to camp blood after a certain amount of time.")
	@ConfigEditorBoolean
	var bloodCampAfterTime = false

	@Expose
	@JvmField
	@ConfigOption(name = "Time (in seconds)", desc = "Time until blood camp warning.")
	@ConfigEditorSlider(minValue = 1f, maxValue = 30f, minStep = 1f)
	var timeTillBloodCamp = 30

	@Expose
	@JvmField
	@ConfigOption(name = "Simon Says Timer", desc = "Announces how much time it took to complete the Simon Says device.")
	@ConfigEditorBoolean
	var ssDeviceTimer = false

	@Expose
	@JvmField
	@ConfigOption(name = "SS Time in Party Chat", desc = "Instead of the message being client-side, send it in party chat.")
	@ConfigEditorBoolean
	var ssDeviceTimerPC = false

	@Expose
	@JvmField
	@ConfigOption(name = "Refill Pearls", desc = "Enables §e/noba refillPearls§r and the associated keybind, grabbing ender pearls out of your sacks when used.")
	@ConfigEditorBoolean
	var refillPearls = false

	@Expose
	@JvmField
	@Category(name = "UAYOR", desc = "Features of potentially questionable legality; use at your own risk")
	var uayor = UAYOR()

	class UAYOR {
		@Expose
		@JvmField
		@ConfigOption(name = "Auto Refill Pearls", desc = "Automatically refill your pearls on dungeon start instead of pressing a key bind or running a command")
		@ConfigEditorBoolean
		var autoRefillPearls = false
	}
}
