package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.MOD_PREFIX
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.*
import kotlin.time.Duration.Companion.seconds

object ChatUtils {
    private var lastMessageSent = Timestamp.distantPast()
    private val commandQueue: Queue<String> = LinkedList()
    private val messageDelay = 1.seconds

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        val player = mc.thePlayer
        if (player == null) {
            commandQueue.clear()
            return
        }
        if (lastMessageSent.elapsedSince() > messageDelay) {
            sendCommand(commandQueue.poll() ?: return)
            lastMessageSent = Timestamp.currentTime()
        }
    }

    fun queueCommand(message: String) {
        commandQueue.add(message)
    }

    private fun sendCommand(command: String) {
        mc.thePlayer.sendChatMessage(
            (if (NobaAddons.config.removeSlash) "" else "/") + command
        )
    }

    fun chatLink(
        message: String,
        url: String,
        hover: String = "§3Open $url",
        prefix: Boolean = true
    ) {
        val usePrefix = if (prefix) MOD_PREFIX else ""
        val text = ChatComponentText(usePrefix + message)
        text.chatStyle.chatHoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText(hover))
        text.chatStyle.chatClickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
        addMessage(text)
    }

    fun sendMessage(message: String) {
        mc.thePlayer.sendChatMessage(message)
    }

    fun delayedSend(message: String) = TickDelay(2) {
        sendMessage(message)
    }

    fun addMessage(prefix: Boolean, message: String) {
        val usePrefix = if (prefix) MOD_PREFIX else ""
        mc.thePlayer.addChatMessage(
            ChatComponentText(
                usePrefix + message
            )
        )
    }

    fun addMessage(message: String) {
        addMessage(true, message)
    }

    fun addMessage(message: ChatComponentText) {
        mc.thePlayer.addChatMessage(message)
    }

    fun delayedAdd(prefix: Boolean, message: String) = TickDelay(1) {
        addMessage(prefix, message)
    }

    fun delayedAdd(message: String) {
        delayedAdd(true, message)
    }
}