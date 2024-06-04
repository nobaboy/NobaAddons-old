package me.nobaboy.nobaaddons.features.notifiers.misc

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.SkyblockUtils
import me.nobaboy.nobaaddons.util.SoundUtils
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
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
                    SoundUtils.repeatSound(100L, 5, "note.pling", 2.0F)
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

        val itemDisplayName = heldItem.displayName.cleanString()
        if (itemDisplayName == "Totem of Corruption") startTimer()
    }

    fun isEnabled() = NobaAddons.config.notifiers.corruptionTotemNotifier && SkyblockUtils.inSkyblock
}