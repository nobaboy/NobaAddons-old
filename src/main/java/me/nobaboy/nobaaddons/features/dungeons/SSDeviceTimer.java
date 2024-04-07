package me.nobaboy.nobaaddons.features.dungeons;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.Utils;
import net.minecraft.block.BlockButtonStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSDeviceTimer {
    final Pattern chatPattern = Pattern.compile("^(?<username>[A-z0-9_]+) completed a device! \\(([0-7])/7\\)");
    boolean inGoldorPhase = false;
    boolean firstButtonPressed = false;
    long deviceStartTime;
    static double timeTakenToEnd;

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        inGoldorPhase = false;
        firstButtonPressed = false;
    }

    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent event) {
        if(!Utils.isInDungeons() || !NobaAddons.config.ssDeviceTimer) return;
        if(Utils.isInCatacombs7()) {
            String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());

            if (receivedMessage.equals("[BOSS] Goldor: Who dares trespass into my domain?")) {
                inGoldorPhase = true;
            } else if (receivedMessage.equals("[BOSS] Goldor: FINALLY! This took way too long.") || receivedMessage.equals("[BOSS] Goldor: Necron, forgive me.")) {
                inGoldorPhase = false;
                firstButtonPressed = false;
            }

            if (!inGoldorPhase) return;
            Matcher chatMatcher = chatPattern.matcher(receivedMessage);
            if (!chatMatcher.find()) return;
            String username = chatMatcher.group("username");
            if (!username.equals(NobaAddons.getUsername())) {
                inGoldorPhase = false;
                firstButtonPressed = false;
                return;
            }
            inGoldorPhase = false;
            firstButtonPressed = false;

            // Time stuff
            timeTakenToEnd = (double) (System.currentTimeMillis() - deviceStartTime) / 1000;
            Double personalBest = SSFile.INSTANCE.personalBest.get();
            List<Double> times = SSFile.INSTANCE.times.get();
            times.add(timeTakenToEnd);

            if (personalBest == null) {
                personalBest = timeTakenToEnd;
                SSFile.INSTANCE.personalBest.set(personalBest);
            }

            if (timeTakenToEnd < personalBest) {
                SSFile.INSTANCE.personalBest.set(timeTakenToEnd);
            }

            // Send message
            String message = (timeTakenToEnd <= personalBest) ? " §3§l(PB)" : " §3(" + personalBest + ")";
            if (NobaAddons.config.ssDeviceTimerPC) {
                ChatUtils.delayedSend("pc Simon Says took " + timeTakenToEnd + "s to complete." + StringUtils.stripControlCodes(message));
            } else {
                ChatUtils.addMessage("Simon Says took " + timeTakenToEnd + "s to complete." + message);
            }

            // Save ss times
            try {
                SSFile.INSTANCE.save();
            } catch (IOException e) {
                NobaAddons.LOGGER.error("Failed to save new time/personal best", e);
            }
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) { // Very unreadable but bare with it.
        if(!Utils.isInDungeons() || !NobaAddons.config.ssDeviceTimer || firstButtonPressed || !inGoldorPhase) return;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(event.entityPlayer != player || !event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) return;
        IBlockState blockState = event.world.getBlockState(event.pos);
        if(blockState == null || blockState.getBlock() == null || !(blockState.getBlock() instanceof BlockButtonStone)) return;
        firstButtonPressed = true;
        deviceStartTime = System.currentTimeMillis();
    }
}