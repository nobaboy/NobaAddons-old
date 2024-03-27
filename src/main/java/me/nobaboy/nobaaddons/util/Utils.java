package me.nobaboy.nobaaddons.util;

import me.nobaboy.nobaaddons.NobaAddons;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.List;

public class Utils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean onHypixel;
    public static boolean inSkyblock;
    public static boolean inDungeons;

    @SubscribeEvent
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if(mc != null) {
            if(NobaAddons.config.debugMode) {
                onHypixel = true;
                return;
            }
            try {
                onHypixel = !event.isLocal && ((mc.thePlayer.getClientBrand().toLowerCase().contains("hypixel")
                        || mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")));
            } catch(NullPointerException e) {
                NobaAddons.LOGGER.warn("Failed to check if player is on hypixel", e);
            }
        }
    }

    public static boolean isOnHypixel() {
        if(NobaAddons.config.debugMode) return true;
        try {
            if(mc != null && mc.theWorld != null && !mc.isSingleplayer()) {
                if(mc.thePlayer != null && mc.thePlayer.getClientBrand() != null) {
                    if(mc.thePlayer.getClientBrand().toLowerCase().contains("hypixel")) {
                        onHypixel = true;
                        return true;
                    }
                }
                if(mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
                    onHypixel = true;
                    return true;
                }
            }
            onHypixel = false;
            return false;
        } catch(Exception e) {
            NobaAddons.LOGGER.error("Failed to check if player is on hypixel", e);
            return false;
        }
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if(onHypixel) onHypixel = false;
    }

    /**
     * Taken from Danker's Skyblock Mod under GPL 3.0 license
     * <a href="https://github.com/bowser0000/SkyblockMod/blob/master/COPYING">link</a>
     * @author bowser0000
     */
    public static void checkForSkyblock() {
        if(NobaAddons.config.debugMode) { inSkyblock = true; return; }
        if(isOnHypixel()) {
            ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if(scoreboardObj != null) {
                String scObjName = ScoreboardUtil.cleanSB(scoreboardObj.getDisplayName());
                if(scObjName.contains("SKYBLOCK") || scObjName.contains("SKIBLOCK")) {
                    inSkyblock = true;
                    return;
                }
            }
        }
        inSkyblock = false;
    }

    /**
     * Taken from Danker's Skyblock Mod under GPL 3.0 license
     * <a href="https://github.com/bowser0000/SkyblockMod/blob/master/COPYING">link</a>
     * @author bowser0000
     */
    public static void checkForDungeons() {
        if(NobaAddons.config.debugMode) { inDungeons = true; return; }
        if(inSkyblock) {
            List<String> scoreboard = ScoreboardUtil.getSidebarLines();
            for(String s : scoreboard) {
                String sCleaned = ScoreboardUtil.cleanSB(s);
                if((sCleaned.contains("The Catacombs") && !sCleaned.contains("Queue")) || sCleaned.contains("Dungeon Cleared:")) {
                    inDungeons = true;
                    return;
                }
            }
        }
        inDungeons = false;
    }
}
