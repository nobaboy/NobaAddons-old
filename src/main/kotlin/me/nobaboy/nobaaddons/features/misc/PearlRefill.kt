package me.nobaboy.nobaaddons.features.misc

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.*
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.StringUtils.lowercaseEquals
import me.nobaboy.nobaaddons.util.data.Location
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object PearlRefill : CooldownManager() {

    private fun getPearlsNeeded(): Int {
        if (!isEnabled()) return 0

        var sum = 0
        for (i in 0 until mc.thePlayer.inventory.sizeInventory) {
            val itemStack: ItemStack? = mc.thePlayer.inventory.getStackInSlot(i)
            if (itemStack?.item != Items.ender_pearl) continue
            if (!itemStack?.displayName!!.cleanString().lowercaseEquals("ender pearl")) continue

            sum += itemStack.stackSize
        }
        return (16 - sum).coerceAtLeast(0)
    }

    fun refillPearls(fromKeyBind: Boolean) {
        if (!isEnabled()) return

        val pearlsToRefill = getPearlsNeeded()
        if (fromKeyBind || LocationUtils.isInLocation(Location.CATACOMBS) || LocationUtils.isInLocation(Location.KUUDRA)) {
            if (pearlsToRefill == 0) {
                ChatUtils.addMessage("Can't add more than 16 pearls to your inventory.")
                return
            }

            HypixelCommands.getFromSacks("ENDER_PEARL", pearlsToRefill)
        }
    }

    @SubscribeEvent
    fun onWorldLoad(ignored: WorldEvent.Load) {
        if (!NobaAddons.config.dungeons.uayor.autoRefillPearls) return
        if (isOnCooldown()) return

        TickDelay(5 * 20) {
            refillPearls(false)
        }
        startCooldown()
    }

    fun isEnabled() = NobaAddons.config.dungeons.refillPearls && mc.theWorld != null && LocationUtils.inSkyblock
}