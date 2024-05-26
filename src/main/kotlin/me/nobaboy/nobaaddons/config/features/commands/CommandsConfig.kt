package me.nobaboy.nobaaddons.config.features.commands

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class CommandsConfig {
	@Expose
	@JvmField
	@ConfigOption(
		name = "Auto Open /swiki Search",
		desc = "Automatically opens the official SkyBlock Wiki with the given search query when using §b/swiki§r."
	)
	@ConfigEditorBoolean
	var autoOpenSWiki = false
}
