package me.nobaboy.nobaaddons.config.features.chat.chatfilter

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Accordion
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption
import me.nobaboy.nobaaddons.config.features.chat.chatfilter.dungeons.DungeonChatFilterConfig
import me.nobaboy.nobaaddons.config.features.chat.chatfilter.general.GeneralChatFilterConfig

class ChatFilterConfig {
    enum class MessageState(private val str: String) {
        SHOWN("Shown"),
        COMPACT("Compact"),
        HIDDEN("Hidden");

        override fun toString(): String {
            return str
        }
    }

    @Expose
    @JvmField
    @ConfigOption(name = "General", desc = "")
    @Accordion
    var general = GeneralChatFilterConfig()

    @Expose
    @JvmField
    @ConfigOption(name = "Dungeons", desc = "")
    @Accordion
    var dungeons = DungeonChatFilterConfig()
}