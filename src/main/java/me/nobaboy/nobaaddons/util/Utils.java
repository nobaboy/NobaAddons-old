package me.nobaboy.nobaaddons.util;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.locations.DungeonFloor;
import me.nobaboy.nobaaddons.util.locations.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;

import java.util.Collection;
import java.util.List;

/**
 * Taken from Danker's Skyblock Mod under GPL 3.0 license
 * <a href="https://github.com/bowser0000/SkyblockMod/blob/master/COPYING">link</a>
 * @author bowser0000
 */
public class Utils {
    public static boolean inSkyblock;
    public static Location currentLocation = Location.NONE;
    public static DungeonFloor currentFloor = DungeonFloor.NONE;

    @SuppressWarnings("all")
    public static boolean isOnHypixel() {
        if(NobaAddons.config.debugMode) return true;

        Minecraft mc = Minecraft.getMinecraft();
        if(mc != null && mc.theWorld != null && !mc.isSingleplayer()) {
            if(mc.thePlayer != null && mc.thePlayer.getClientBrand() != null && mc.thePlayer.getClientBrand().toLowerCase().contains("hypixel")) {
                return true;
            }
            if(mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
                return true;
            }
        }
        return false;
    }

    public static void checkForSkyblock() {
        if(NobaAddons.config.debugMode) {
            inSkyblock = true;
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if(mc != null && mc.theWorld != null && !mc.isSingleplayer()) {
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

    public static void checkTabLocation() {
        if(inSkyblock) {
            Collection<NetworkPlayerInfo> players = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
            for(NetworkPlayerInfo player : players) {
                if(player == null || player.getDisplayName() == null) continue;
                String text = player.getDisplayName().getUnformattedText();
                if(text.startsWith("Area: ") || text.startsWith("Dungeon: ")) {
                    currentLocation = Location.fromTab(text.substring(text.indexOf(":") + 2));
                    return;
                }
            }
        }
        currentLocation = Location.NONE;
    }

    public static boolean isInDungeons() {
        return currentLocation == Location.CATACOMBS;
    }

    public static void checkForDungeonFloor() {
        if(isInDungeons()) {
            List<String> scoreboard = ScoreboardUtil.getSidebarLines();

            for(String s : scoreboard) {
                String sCleaned = ScoreboardUtil.cleanSB(s);

                if(sCleaned.contains("The Catacombs (")) {
                    String floor = sCleaned.substring(sCleaned.indexOf("(") + 1, sCleaned.indexOf(")"));

                    try {
                        currentFloor = DungeonFloor.valueOf(floor);
                    } catch(IllegalArgumentException ex) {
                        currentFloor = DungeonFloor.NONE;
                        ex.printStackTrace();
                    }

                    break;
                }
            }
        } else {
            currentFloor = DungeonFloor.NONE;
        }
    }

    // Will get a use later on.
    public static boolean isInScoreboard(String text) {
        List<String> scoreboard = ScoreboardUtil.getSidebarLines();
        for(String s : scoreboard) {
            String sCleaned = ScoreboardUtil.cleanSB(s);
            if(sCleaned.contains(text)) return true;
        }
        return false;
    }
}
