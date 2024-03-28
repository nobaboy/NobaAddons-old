package me.nobaboy.nobaaddons.features.dungeons;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PearlRefill {
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        checkBeforeGetting();
    }

    public static void refillPearls() {
        if(!NobaAddons.config.refillPearls) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        int sum = 0;
        for(int i=0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack itemStack = player.inventory.getStackInSlot(i);
            if(itemStack != null && itemStack.getItem() == Items.ender_pearl && StringUtils.stripControlCodes(itemStack.getDisplayName()).equalsIgnoreCase("ender pearl")) {
                sum += itemStack.stackSize;
            }
        }

        int toAdd = Math.max(16 - sum, 0);
        if(toAdd > 0) ChatUtils.sendCommand("gfs ENDER_PEARL " + toAdd);
    }

    public synchronized void checkBeforeGetting() {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch(InterruptedException ignored) { }
            if(!Utils.isInDungeons() || !NobaAddons.config.autoRefillPearls) return;
            refillPearls();
        }).start();
    }
}
