package me.nobaboy.nobaaddons.core

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.Config
import io.github.notenoughupdates.moulconfig.annotations.Category
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption
import me.nobaboy.nobaaddons.NobaAddons
import java.io.File

class NobaConfig : Config() {
    companion object {
        val FILE = File("./config/nobaaddons/config.toml")
    }

    override fun getTitle() = "NobaAddons ${NobaAddons.MOD_VERSION}"

    @Expose
    @JvmField
    @Category(name = "Commands", desc = "Commands")
    var commands = CommandsConfig()

    @Expose
    @JvmField
    @Category(name = "Chat", desc = "Chat related features")
    var chat = ChatConfig()

    @Expose
    @JvmField
    @Category(name = "Chat Commands", desc = "Chat Commands")
    var chatCommands = ChatCommandsConfig()

    @Expose
    @JvmField
    @Category(name = "Notifiers", desc = "Notifications for certain things")
    var notifiers = NotifiersConfig()

    @Expose
    @JvmField
    @Category(name = "Dungeons", desc = "Dungeons related features")
    var dungeons = DungeonsConfig()

    @Expose
    @JvmField
    @Category(name = "Dev", desc = "Development options; you should leave these alone if you don't know what you're doing.")
    var dev = Dev()

    class Dev {
        @Expose
        @JvmField
        @ConfigOption(name = "Debug Mode", desc = "Enables debug functionality. §cOnly turn this on if you were explicitly asked to.")
        @ConfigEditorBoolean
        var debugMode = false

        @Expose
        @JvmField
        @ConfigOption(name = "Remove Slash from Messages", desc = "Removes the slash from command messages sent by the mod. §cOnly turn this on if you know what you're doing.")
        @ConfigEditorBoolean
        var removeSlash = false
    }
}
