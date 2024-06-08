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
    @ConfigOption(name = "Options", desc = "§9§lShown: §rDisplays the message as it is.\n§9§lCompact: §Condenses the message into a single line.\n§9§lHidden: §rPrevents the message from appearing in chat.")
    @ConfigEditorInfoText
    var displayOptions: Nothing? = null

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
    @ConfigOption(name = "Disable Pickup/Obtain Messages", desc = "Disables the display of messages for picking up or obtaining items in chat.")
    @ConfigEditorBoolean
    var pickupObtainMessages = false

    @Expose
    @JvmField
    @ConfigOption(name = "Allow 50/50 Items", desc = "Allow messages for obtaining 50/50 items.\n§cNote: Pickup/Obtain Messages must be enabled.")
    @ConfigEditorBoolean
    var allow5050ItemMessage = false
}