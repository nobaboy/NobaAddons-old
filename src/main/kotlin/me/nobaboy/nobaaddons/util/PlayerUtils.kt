package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import net.minecraft.inventory.ContainerChest

object PlayerUtils {
    sealed class ClickMode {
        data object Left : ClickMode()
        data object Right : ClickMode()
        data object Middle : ClickMode()
        data object Shift : ClickMode()
    }

    private data class SlotClick(val slotId: Int, val button: Int, val mode: Int)

    private val clickQueue = mutableListOf<SlotClick>()

    fun resetClickQueue() {
        if (clickQueue.isNotEmpty()) clickQueue.clear()
    }

    fun enqueueSlotClick(slotId: Int, button: Int, mode: Int, instant: Boolean = false) {
        if (instant) executeSlotClick(slotId, button, mode)
        else clickQueue.add(SlotClick(slotId, button, mode))
    }

    fun slotClick(slotId: Int, clickType: ClickMode, instant: Boolean = false) {
        when (clickType) {
            is ClickMode.Left -> enqueueSlotClick(slotId, 0, 0, instant)
            is ClickMode.Right -> enqueueSlotClick(slotId, 1, 0, instant)
            is ClickMode.Middle -> enqueueSlotClick(slotId, 2, 3, instant)
            is ClickMode.Shift -> enqueueSlotClick(slotId, 0, 1, instant)
        }
    }

    fun processClickQueue() {
        if (mc.thePlayer?.openContainer == null) return clickQueue.clear()
        if (clickQueue.isEmpty()) return

        clickQueue.first().apply {
            try {
                executeSlotClick(slotId, button, mode)
            } catch (ex: Exception) {
                NobaAddons.LOGGER.error("Error sending window click: $this")
                ex.printStackTrace()
                clickQueue.clear()
            }
        }
        clickQueue.removeFirstOrNull()
    }

    private fun executeSlotClick(slotId: Int, button: Int, mode: Int) {
        mc.thePlayer?.openContainer?.let {
            if (it !is ContainerChest) {
                clickQueue.clear()
                return@let
            }
            mc.playerController.windowClick(it.windowId, slotId, button, mode, mc.thePlayer)
        }
    }
}