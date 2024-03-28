package me.nobaboy.nobaaddons.features.dungeons;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BloodCampTimer {
    static boolean announcing = false;
    static AnnounceCampWarning announceCampWarning;

    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent event) {
        if(!NobaAddons.config.bloodCampAfterTime || !Utils.isInDungeons()) return;
        String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());

        if(announcing) return;
        if(receivedMessage.startsWith("The BLOOD DOOR has been opened!")) {
            announcing = true;
            startThread();
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if(announceCampWarning != null) {
            announceCampWarning.interrupt();
            announcing = false;
        }
    }

    public static class AnnounceCampWarning extends Thread {
        @Override
        public void run() {
            try {
                sleep(1000L * NobaAddons.config.timeTilBloodCamp);
            } catch(InterruptedException ignored) { return; }
            ChatUtils.addMessage("Go camp blood!");
            for(int i = 0; i < 10; i++) {
                Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1, 2.0f);
                try {
                    sleep(100);
                } catch(InterruptedException ignored) { return; }
            }
            announcing = false;
        }
    }

    public void startThread() {
        announceCampWarning = new AnnounceCampWarning();
        announceCampWarning.setName("blood-camp-warning");
        announceCampWarning.start();
    }
}