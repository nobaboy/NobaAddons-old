package me.nobaboy.nobaaddons.util;

import me.nobaboy.nobaaddons.NobaAddons;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import static net.minecraft.util.EnumChatFormatting.*;

public class ChatUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static final String MOD_PREFIX = BLUE + "NobaAddons " + DARK_BLUE + "> " + AQUA;

    public static void sendCommand(String command) {
        command = (!NobaAddons.config.debugMode ? "/" : "") + command;
        mc.thePlayer.sendChatMessage(command);
    }

    public static void addMessage(boolean prefix, String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText(prefix ? MOD_PREFIX + message : message));
    }

    public static void addMessage(String message) {
        addMessage(true, message);
    }

    public static void delayedSend(String command) {
        new TickDelay(() -> sendCommand(command), 2);
    }

    public static void delayedAdd(boolean prefix, String message) {
        new TickDelay(() -> addMessage(prefix, message), 1);
    }

    public static void delayedAdd(String message) {
        delayedAdd(true, message);
    }
}
