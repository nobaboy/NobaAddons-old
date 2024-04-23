package me.nobaboy.nobaaddons.features.dungeons;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.CooldownManager;
import me.nobaboy.nobaaddons.util.TickDelay;
import me.nobaboy.nobaaddons.util.Utils;
import me.nobaboy.nobaaddons.util.data.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PearlRefill extends CooldownManager {
    public static void refillPearls(boolean fromKeyBind) {
        if(!Utils.inSkyblock || !NobaAddons.config.refillPearls) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        int sum = 0;
        for(int i=0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack itemStack = player.inventory.getStackInSlot(i);
            if(itemStack != null && itemStack.getItem() == Items.ender_pearl && StringUtils.stripControlCodes(itemStack.getDisplayName()).equalsIgnoreCase("ender pearl")) {
                sum += itemStack.stackSize;
            }
        }
        int toAdd = Math.max(16 - sum, 0);

        if(fromKeyBind || Utils.isInLocation(Location.CATACOMBS) || Utils.isInLocation(Location.KUUDRA)) {
            if(toAdd > 0) {
                ChatUtils.sendCommand("gfs ENDER_PEARL " + toAdd);
            } else {
                ChatUtils.addMessage("Can't add more than 16 pearls to your inventory.");
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        if(!NobaAddons.config.autoRefillPearls || isOnCooldown()) return;
        new TickDelay(() -> refillPearls(false), 5 * 20);
        startCooldown(10 * 1000);
    }
}
