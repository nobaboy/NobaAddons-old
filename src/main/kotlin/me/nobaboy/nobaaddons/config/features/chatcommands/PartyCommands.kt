package me.nobaboy.nobaaddons.config.features.chatcommands

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class PartyCommands {
    @Expose
    @JvmField
    @ConfigOption(name = "Party Commands", desc = "Enable chat commands in party chat. §3/noba help partycommands§r")
    @ConfigEditorBoolean
    var enabled = false

    @Expose
    @JvmField
    @ConfigOption(name = "!help", desc = "Replies with all usable commands.")
    @ConfigEditorBoolean
    var help = false

    @Expose
    @JvmField
    @ConfigOption(name = "!transfer [username]", desc = "Transfer the party to the player who ran the command or the optional username. §7(alias: !pt [username], !ptme)§r")
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