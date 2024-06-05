package me.nobaboy.nobaaddons.features.chat

import me.nobaboy.nobaaddons.features.chat.chatfilter.DungeonFilters
import me.nobaboy.nobaaddons.features.chat.chatfilter.GeneralFilters
import me.nobaboy.nobaaddons.util.SkyblockUtils
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.data.Location
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ChatFilter {
    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        val receivedMessage = event.message.unformattedText.cleanString()

        GeneralFilters.processFilters(event, receivedMessage)

        when (SkyblockUtils.getPlayerLocation()) {
            Location.CATACOMBS -> DungeonFilters.processFilters(event, receivedMessage)
            else -> {}
        }
    }
}