package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.*
import kotlin.time.Duration.Companion.seconds

object ChatUtils {
    private var lastMessageSent = TimeStamp.distantPast()
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
            lastMessageSent = TimeStamp.currentTime()
        }
    }

    fun queueCommand(message: String) {
        commandQueue.add(message)
    }

    fun sendCommand(command: String) {
        mc.thePlayer.sendChatMessage(
            (if (NobaAddons.config.removeSlash) "" else "/") + command
        )
    }

    fun sendMessage(message: String) {
        mc.thePlayer.sendChatMessage(message)
    }

    fun delayedSend(message: String) = TickDelay(2) {
        sendMessage(message)
    }

    fun addMessage(prefix: Boolean, message: String) {
        mc.thePlayer.addChatMessage(
            ChatComponentText(
                if (prefix) NobaAddons.MOD_PREFIX + message else message
            )
        )
    }

    fun addMessage(message: String) {
        addMessage(true, message)
    }

    fun delayedAdd(prefix: Boolean, message: String) = TickDelay(1) {
        addMessage(prefix, message)
    }

    fun delayedAdd(message: String) {
        delayedAdd(true, message)
    }
}