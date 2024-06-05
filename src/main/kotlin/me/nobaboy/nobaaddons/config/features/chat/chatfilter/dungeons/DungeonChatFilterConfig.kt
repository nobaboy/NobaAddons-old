package me.nobaboy.nobaaddons.config.features.chat.chatfilter.dungeons

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorInfoText
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption
import io.github.notenoughupdates.moulconfig.observer.Property
import me.nobaboy.nobaaddons.config.features.chat.chatfilter.ChatFilterConfig.MessageState

class DungeonChatFilterConfig {
    @Expose
    @JvmField
    @ConfigOption(name = "Note", desc = "§9§lShown: §rKeeps the message as is.\n§9§lCompact: §rCompacts the message into one line.\n§9§lHidden: §rHides the message from showing in chat.")
    @ConfigEditorInfoText
    var usageNote: Nothing? = null

    @Expose
    @JvmField
    @ConfigOption(name = "Blessings Message", desc = "§8Compact:\n§c§lPOWER BUFF!§7 +10.6 & +1.05x ❁ Strength and ☠ Crit Damage.")
    @ConfigEditorDropdown
    var blessingsMessage: Property<MessageState> = Property.of(MessageState.SHOWN)

    @Expose
    @JvmField
    @ConfigOption(name = "Healer Orb Message", desc = "§8Compact:\n§e§lHEALER ORB! §c+598.3❤ §7and §a+20% Ability Damage §7for picking up nobaboy's Ability Damage Orb.")
    @ConfigEditorDropdown
    var healerOrbMessage: Property<MessageState> = Property.of(MessageState.SHOWN)

    @Expose
    @JvmField
    @ConfigOption(name = "Pickup/Obtain Messages", desc = "Toggles the pickup/obtain messages in chat.")
    @ConfigEditorBoolean
    var pickupObtainMessages = false
}