package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

object ContainerUtils {
    val ContainerChest.name: String
        get() = this.lowerChestInventory.displayName.unformattedText.cleanString()

    val Container.name: String
        get() = (this as? ContainerChest)?.name ?: "Undefined Container"

    private fun getOpenChestItems(): List<Slot> {
        val guiChest = mc.currentScreen as? GuiChest ?: return emptyList()
        return guiChest.inventorySlots.inventorySlots
            .takeWhile { it.inventory !is InventoryPlayer }
            .filter { it.stack != null }
    }

    fun getItemAtSlot(slotIndex: Int): ItemStack? {
        return getOpenChestItems().find { it.slotIndex == slotIndex }?.stack
    }

    fun ItemStack.getLore(): List<String> {
        val tagCompound = this.tagCompound ?: return emptyList()
        val displayTag = tagCompound.getCompoundTag("display")
        val loreTagList = displayTag.getTagList("Lore", 8)
        return List(loreTagList.tagCount()) { loreTagList.getStringTagAt(it) }
    }
}