package me.nobaboy.nobaaddons.config.features.chatcommands

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class GuildCommands {
    @Expose
    @JvmField
    @ConfigOption(name = "Guild Commands", desc = "Enable chat commands in guild chat. §3/noba help guildcommands§r")
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