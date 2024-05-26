package me.nobaboy.nobaaddons.core

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Category
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

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

	class DMCommands {
		@Expose
		@JvmField
		@ConfigOption(name = "DM Commands", desc = "Enable chat commands when people §e/msg§r you. §e/noba help dmcommands§r")
		@ConfigEditorBoolean
		var enabled = false

		@Expose
		@JvmField
		@ConfigOption(name = "!help", desc = "Replies with all usable commands.")
		@ConfigEditorBoolean
		var help = false

		@Expose
		@JvmField
		@ConfigOption(name = "!warpme", desc = "Warps the messaging user to your lobby.")
		@ConfigEditorBoolean
		var warpMe = false

		@Expose
		@JvmField
		@ConfigOption(name = "!partyme", desc = "Invites the messaging user to a party. §7(alias: !pme)§r")
		@ConfigEditorBoolean
		var partyMe = false

		@Expose
		@JvmField
		@ConfigOption(name = "!warpout", desc = "Warps the specified user.")
		@ConfigEditorBoolean
		var warpOut = false
	}

	class PartyCommands {
		@Expose
		@JvmField
		@ConfigOption(name = "Party Commands", desc = "Enable chat commands in party chat. §e/noba help partycommands§r")
		@ConfigEditorBoolean
		var enabled = false

		@Expose
		@JvmField
		@ConfigOption(name = "!help", desc = "Replies with all usable commands.")
		@ConfigEditorBoolean
		var help = false

		@Expose
		@JvmField
		@ConfigOption(name = "!transfer", desc = "Transfer the party to the player who ran the command. §7(alias: !ptme)§r")
		@ConfigEditorBoolean
		var transfer = false

		@Expose
		@JvmField
		@ConfigOption(name = "!allinvite", desc = "Toggles §e/p settings allinvite§r when used. §7(alias: !allinv)§r")
		@ConfigEditorBoolean
		var allInvite = false

		@Expose
		@JvmField
		@ConfigOption(name = "!warp", desc = "Runs §e/p warp§r when used, with an optional delay.")
		@ConfigEditorBoolean
		var warp = false

		@Expose
		@JvmField
		@ConfigOption(name = "!coords", desc = "Sends your current coordinates in party chat when used.")
		@ConfigEditorBoolean
		var coords = false
	}

	class GuildCommands {
		@Expose
		@JvmField
		@ConfigOption(name = "Guild Commands", desc = "Enable chat commands in guild chat. §e/noba help guildcommands§r")
		@ConfigEditorBoolean
		var enabled = false

		@Expose
		@JvmField
		@ConfigOption(name = "!help", desc = "Replies with all usable commands.")
		@ConfigEditorBoolean
		var help = false

		@Expose
		@JvmField
		@ConfigOption(name = "!warpout", desc = "Warps the specified user.")
		@ConfigEditorBoolean
		var warpOut = false
	}
}
