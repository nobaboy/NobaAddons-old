package me.nobaboy.nobaaddons.util

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class TickDelay(private var waitingTicks: Int, val task: () -> Unit) {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.START) {
            if (waitingTicks < 1) {
                task()
                MinecraftForge.EVENT_BUS.unregister(this)
            }
            waitingTicks--
        }
    }
}