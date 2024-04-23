package me.nobaboy.nobaaddons.features.notifiers;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class QOLNotifiers {
    int cakesEaten = 0;
    static int seconds;
    static NotifyTotem notifyTotem = new NotifyTotem();

    private static class NotifyTotem extends Thread {
        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            seconds = 120;
            while(seconds >= 0) {
                if(--seconds == 0) {
                    ChatUtils.addMessage("Place Totem of Corruption!");
                    for(int i = 0; i < 5; i++) {
                        Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1, 2.0f);
                        try {
                            sleep(100);
                        } catch(InterruptedException ignored) { break; }
                    }
                    break;
                }
                try {
                    sleep(1000);
                } catch(InterruptedException ignored) { break; }
            }
        }
    }

    public void startTotemTimer() {
        notifyTotem.interrupt();
        notifyTotem = new NotifyTotem();
        notifyTotem.start();
    }

    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent e) {
        if(!NobaAddons.config.cakesEatenNotifier || !Utils.inSkyblock) return;
        String message = e.message.getUnformattedText().toLowerCase();
        if(message.startsWith("yum! you gain")) {
            cakesEaten++;
            if(cakesEaten >= NobaAddons.config.centuryCakesAmount) {
                ChatUtils.delayedAdd("All cakes eaten!");
                Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1, 2.5f);
                cakesEaten = 0;
            }
        }
    }

    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent event) {
        if(!NobaAddons.config.corruptionTotemNotifier || !Utils.inSkyblock) return;
        if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.entityPlayer == Minecraft.getMinecraft().thePlayer) {
            ItemStack heldItem = event.entityPlayer.getHeldItem();
            if(heldItem != null && heldItem.getItem() == Items.banner) {
                String heldItemName = StringUtils.stripControlCodes(heldItem.getDisplayName());
                if(heldItemName.equals("Totem of Corruption")) startTotemTimer();
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        notifyTotem.interrupt();
    }
}