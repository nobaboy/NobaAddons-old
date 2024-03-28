package me.nobaboy.nobaaddons.features.chatcommands;

import com.google.common.collect.Lists;
import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.CooldownManager;
import me.nobaboy.nobaaddons.util.PartyUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class PartyCommands extends CooldownManager {
    List<String> commands = Lists.newArrayList("help", "pt", "ptme", "transfer", "allinvite", "allinv", "warp", "cancel", "coords");

    // Warp command
    static int delay = 0;
    static boolean isWarping = false;
    static boolean cancel = false;

    final Pattern chatPattern = Pattern.compile("^Party > (?:\\[[A-Z+]+] )?(?<username>[A-z0-9_]+): !(?<command>[A-z0-9_]+) ?(?<argument>[A-z0-9_]+)?");

    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent event) {
        if(!NobaAddons.config.partyCommands) return;
        String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());

        Matcher chatMatcher = chatPattern.matcher(receivedMessage);
        if(!chatMatcher.find()) return;
        String command = chatMatcher.group("command");
        String argument = chatMatcher.group("argument");
        String sender = chatMatcher.group("username");

        if(!NobaAddons.config.debugMode && (!commands.contains(command.toLowerCase()) || isOnCooldown())) return;
        startCooldown();
        switch(command.toLowerCase()) {
            case "help":
                if(!NobaAddons.config.partyHelpCommand) return;
                ChatUtils.delayedSend("pc NobaAddons > !help, !(pt|ptme|transfer) , !(allinvite|allinv), !warp [optional: seconds], !cancel, !coords");
                break;
            case "pt":
            case "ptme":
            case "transfer":
                if(PartyUtils.isLeader || !NobaAddons.config.transferCommand) return;
                ChatUtils.delayedSend("p transfer " + sender);
                break;
            case "allinvite":
            case "allinv":
                if((!PartyUtils.isLeader && NobaAddons.PLAYER_IGN.equals(sender)) || !NobaAddons.config.allInviteCommand) return;
                ChatUtils.delayedSend("p settings allinvite");
                break;
            case "warp":
                if((!PartyUtils.isLeader && NobaAddons.PLAYER_IGN.equals(sender)) || isWarping || !NobaAddons.config.warpCommand) return;
                warpCommand(argument);
                break;
            case "cancel":
                if(!isWarping || !NobaAddons.config.warpCommand) return;
                cancel = true;
                break;
            case "coords":
                if(!NobaAddons.config.coordsCommand) return;
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                ChatUtils.delayedSend("pc x: " + (int) player.posX + ", y: " + (int) player.posY + ", z: " + (int) player.posZ);
                break;
            default:
                if(NobaAddons.config.debugMode) NobaAddons.LOGGER.warn("Unexpected value while parsing party command: " + command.toLowerCase());
        }
    }

    public void warpCommand(String time) {
        if(time == null) {
            ChatUtils.delayedSend("p warp");
        } else if(!org.apache.commons.lang3.StringUtils.isNumeric(time)) {
            ChatUtils.delayedSend("pc First argument can either be empty or numbers.");
        } else if(parseInt(time) > 15 || parseInt(time) < 3) {
            ChatUtils.delayedSend("pc Warp delay has a maximum limit of 15 seconds and a minimum of 3.");
        } else {
            ChatUtils.delayedSend("pc Warping in " + time + " (To cancel type '!cancel')");
            delay = parseInt(time);
            isWarping = true;
            startTimedWarp();
        }
    }

    @SuppressWarnings("BusyWait")
    public void startTimedWarp() {
        new Thread(() -> {
            int secondsPassed = --delay;
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ignored) {}
                if(cancel) {
                    ChatUtils.sendCommand("pc Warp cancelled...");
                    cancel = false; isWarping = false;
                    break;
                }
                ChatUtils.sendCommand("pc " + secondsPassed);
                if(--secondsPassed == 0) {
                    ChatUtils.sendCommand("p warp");
                    isWarping = false;
                    break;
                }
            }
        }).start();
    }
}