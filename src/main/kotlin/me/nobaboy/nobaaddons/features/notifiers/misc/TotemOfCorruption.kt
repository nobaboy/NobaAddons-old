package me.nobaboy.nobaaddons.features.notifiers.misc

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.Utils
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.StringUtils
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TotemOfCorruption {
    private var timer: Job? = null

    private fun startTimer() {
        timer?.cancel()
        timer = NobaAddons.coroutineScope.launch {
            var seconds = 120
            while (seconds-- >= 0) {
                if (seconds == 0) {
                    ChatUtils.addMessage("Place Totem of Corruption!")
                    for (i in 1..5) {
                        mc.thePlayer.playSound("note.pling", 1F, 2.0F)
                        delay(100.milliseconds)
                    }
                    break
                }
                delay(1.seconds)
            }
        }
    }

    @SubscribeEvent
    fun onWorldUnload(ignored: WorldEvent.Unload) {
        timer?.cancel()
    }

    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!isEnabled()) return
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return
        if (event.entityPlayer != mc.thePlayer) return

        val heldItem: ItemStack = event.entityPlayer.heldItem ?: return
        if (heldItem.item != Items.banner) return

        val itemDisplayName = StringUtils.stripControlCodes(heldItem.displayName)
        if (itemDisplayName == "Totem of Corruption") startTimer()
    }

    fun isEnabled() = NobaAddons.config.corruptionTotemNotifier && Utils.inSkyblock
}