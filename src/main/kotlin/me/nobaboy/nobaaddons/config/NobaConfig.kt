package me.nobaboy.nobaaddons.config

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.Config
import io.github.notenoughupdates.moulconfig.annotations.Category
import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.config.features.chat.ChatConfig
import me.nobaboy.nobaaddons.config.features.chatcommands.ChatCommandsConfig
import me.nobaboy.nobaaddons.config.features.commands.CommandsConfig
import me.nobaboy.nobaaddons.config.features.dev.DevConfig
import me.nobaboy.nobaaddons.config.features.dungeons.DungeonsConfig
import me.nobaboy.nobaaddons.config.features.notifiers.NotifiersConfig
import java.io.File

class NobaConfig : Config() {
    companion object {
        val FILE = File("./config/nobaaddons/config.toml")
    }

    override fun getTitle() = "NobaAddons ${NobaAddons.MOD_VERSION}"

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
    @Category(name = "Commands", desc = "Commands")
    var commands = CommandsConfig()

    @Expose
    @JvmField
    @Category(name = "Dungeons", desc = "Dungeons related features")
    var dungeons = DungeonsConfig()

    @Expose
    @JvmField
    @Category(name = "Notifiers", desc = "Notifications for certain things")
    var notifiers = NotifiersConfig()

    @Expose
    @JvmField
    @Category(name = "Dev", desc = "Development options; you should leave these alone if you don't know what you're doing.")
    var dev = DevConfig()
}
