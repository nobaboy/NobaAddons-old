package me.nobaboy.nobaaddons.config.features.dungeons

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Accordion
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class DungeonsConfig {
	@Expose
	@JvmField
	@ConfigOption(name = "Camp Blood Timer", desc = "")
	@Accordion
	var bloodCampTimer = BloodCampTimerConfig()

	@Expose
	@JvmField
	@ConfigOption(name = "Simon Says Timer", desc = "")
	@Accordion
	var simonSaysTimer = SimonSaysConfig()

	@Expose
	@JvmField
	@ConfigOption(name = "Refill Pearls", desc = "")
	@Accordion
	var refillPearls = RefillPearlsConfig()
}
