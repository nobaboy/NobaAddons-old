package me.nobaboy.nobaaddons.config.features.chatcommands

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Category

class ChatCommandsConfig {
	@Expose
	@JvmField
	@Category(name = "DM Commands", desc = "DM Commands")
	var dmCommands = DMCommands()

	@Expose
	@JvmField
	@Category(name = "Party Commands", desc = "Party Commands")
	var partyCommands = PartyCommands()

	@Expose
	@JvmField
	@Category(name = "Guild Commands", desc = "Guild Commands")
	var guildCommands = GuildCommands()
}
