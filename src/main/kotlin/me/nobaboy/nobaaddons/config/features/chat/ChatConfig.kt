package me.nobaboy.nobaaddons.config.features.chat

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Accordion
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption
import me.nobaboy.nobaaddons.config.features.chat.chatfilter.ChatFilterConfig

class ChatConfig {
	@Expose
	@JvmField
	@ConfigOption(name = "Chat Filter", desc = "")
	@Accordion
	var chatFilter = ChatFilterConfig()
}
