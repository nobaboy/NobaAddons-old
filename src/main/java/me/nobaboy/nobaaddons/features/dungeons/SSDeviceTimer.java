package me.nobaboy.nobaaddons.features.dungeons;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.Utils;
import me.nobaboy.nobaaddons.util.data.DungeonBoss;
import me.nobaboy.nobaaddons.util.data.Location;
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
    final Pattern chatPattern = Pattern.compile("^(?<username>[A-z0-9_]+) completed a device! \\([1-7]/7\\)");
    boolean deviceDone = false;
    boolean firstButtonPressed = false;
    long deviceStartTime;
    double timeTakenToEnd;

    private void processDeviceTime() {
        deviceDone = true;

        timeTakenToEnd = (double) (System.currentTimeMillis() - deviceStartTime) / 1000;
        List<Double> times = SSFile.INSTANCE.times.get();
        times.add(timeTakenToEnd);

        Double personalBest = SSFile.INSTANCE.personalBest.get();
        if(personalBest == null || timeTakenToEnd < personalBest) {
            SSFile.INSTANCE.personalBest.set(timeTakenToEnd);
            personalBest = timeTakenToEnd;
        }

        String isPB = (timeTakenToEnd <= personalBest) ? " §3§l(PB)" : " §3(" + personalBest + ")";
        String message = "Simon Says took " + timeTakenToEnd + "s to complete." + isPB;
        if(NobaAddons.config.ssDeviceTimerPC) {
            ChatUtils.delayedAdd("pc " + message);
        } else {
            ChatUtils.addMessage(message);
        }

        try {
            SSFile.INSTANCE.save();
        } catch (IOException e) {
            NobaAddons.LOGGER.error("Failed to save new time/personal best", e);
        }
    }

    public boolean isApplicable() {
        return NobaAddons.config.ssDeviceTimer && Utils.isInLocation(Location.CATACOMBS);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if(deviceDone || firstButtonPressed) {
            deviceDone = false;
            firstButtonPressed = false;
        }
    }

    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent event) {
        if(!isApplicable() || deviceDone) return;
        if(Utils.isInCatacombs7() && Utils.isInPhase(DungeonBoss.GOLDOR)) {
            String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());

            Matcher chatMatcher = chatPattern.matcher(receivedMessage);
            if(!chatMatcher.find()) return;

            String username = chatMatcher.group("username");
            if(!username.equals(NobaAddons.getUsername())) {
                deviceDone = true;
                return;
            }

            processDeviceTime();
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) { // Very unreadable but bare with it.
        if(!isApplicable() || firstButtonPressed || deviceDone) return;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(event.entityPlayer != player || !event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) return;

        IBlockState blockState = event.world.getBlockState(event.pos);
        if(blockState == null || blockState.getBlock() == null || !(blockState.getBlock() instanceof BlockButtonStone)) return;

        firstButtonPressed = true;
        deviceStartTime = System.currentTimeMillis();
    }
}