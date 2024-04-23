package me.nobaboy.nobaaddons.util;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.data.DungeonBoss;
import me.nobaboy.nobaaddons.util.data.DungeonClass;
import me.nobaboy.nobaaddons.util.data.DungeonFloor;
import me.nobaboy.nobaaddons.util.data.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
    public static DungeonClass currentClass = DungeonClass.NONE;
    public static DungeonBoss currentBoss = DungeonBoss.UNKNOWN;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isOnHypixel() {
        if(NobaAddons.config.debugMode) return true;

        Minecraft mc = Minecraft.getMinecraft();
        if(mc != null && mc.theWorld != null && !mc.isSingleplayer()) {
            if(mc.thePlayer != null && mc.thePlayer.getClientBrand() != null && mc.thePlayer.getClientBrand().toLowerCase().contains("hypixel")) {
                return true;
            }
            return mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel");
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

    public static boolean isInLocation(Location location) {
        return currentLocation == location;
    }

    /**
     * Modified logging
     */
    public static void checkForDungeonFloor() {
        if(isInLocation(Location.CATACOMBS)) {
            List<String> scoreboard = ScoreboardUtil.getSidebarLines();

            for(String s : scoreboard) {
                String sCleaned = ScoreboardUtil.cleanSB(s);

                if(sCleaned.contains("The Catacombs (")) {
                    String floor = sCleaned.substring(sCleaned.indexOf("(") + 1, sCleaned.indexOf(")"));

                    try {
                        currentFloor = DungeonFloor.valueOf(floor);
                    } catch(IllegalArgumentException ex) {
                        currentFloor = DungeonFloor.NONE;
                        NobaAddons.LOGGER.error("Unexpected value for Dungeon Floor: ", ex);
                    }

                    break;
                }
            }
        } else {
            currentFloor = DungeonFloor.NONE;
        }
    }

    /**
     * @author nobaboy
     */
    public static boolean isInCatacombs7() {
        return currentFloor == DungeonFloor.F7 || currentFloor == DungeonFloor.M7;
    }

    /**
     * @author nobaboy
     */
    public static void checkForDungeonClass() {
        if(isInLocation(Location.CATACOMBS)) {
            String playerName = Minecraft.getMinecraft().thePlayer.getName();
            Collection<NetworkPlayerInfo> players = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
            for(NetworkPlayerInfo player : players) {
                if(player == null || player.getDisplayName() == null) continue;
                String text = player.getDisplayName().getUnformattedText();
                if(text.contains(playerName) && text.indexOf('(') != -1) {
                    String dungeonClass = text.substring(text.indexOf("(") + 1, text.lastIndexOf(")"));
                    if(dungeonClass.equals("EMPTY")) return;
                    currentClass = DungeonClass.valueOf(dungeonClass.split(" ")[0].toUpperCase());
                    break;
                }
            }
        } else {
            currentClass = DungeonClass.NONE;
        }
    }

    /**
     * @author nobaboy
     */
    @SubscribeEvent
    public void checkForDungeonBoss(final ClientChatReceivedEvent event) {
        String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if(isInLocation(Location.CATACOMBS)) currentBoss = DungeonBoss.fromChat(receivedMessage);
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
