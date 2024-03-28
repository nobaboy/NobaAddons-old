package me.nobaboy.nobaaddons.features.dungeons;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PearlRefill {
    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent event) {
        if(!Utils.isInDungeons()) return;
        String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());

        if(receivedMessage.equals("[NPC] Mort: Good luck.")) refillPearls();
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
}
