package me.nobaboy.nobaaddons.config.features.chatcommands

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class DMCommands {
    @Expose
    @JvmField
    @ConfigOption(name = "DM Commands", desc = "Enable chat commands when people §b/msg§r you. §3/noba help dmcommands§r")
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