package me.nobaboy.nobaaddons.features.chatcommands;

import com.google.common.collect.Lists;
import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.ChatUtils;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DMCommands {
    List<String> commands = Lists.newArrayList("warpme", "partyme", "pme", "warpout");
    static int cooldown = 0;

    // Warp in User
    static boolean isWarpingUser = false;
    static boolean playedJoined = false;
    static String player;

    final Pattern chatPattern = Pattern.compile("^From (?:\\[[A-Z+]+] )?(?<username>[A-z0-9_]+): !(?<command>[A-z0-9_]+) ?(?<argument>[A-z0-9_]+)?");

    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent event) {
        if(!NobaAddons.config.dmCommands) return;
        String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());

        if(isWarpingUser) {
            if(receivedMessage.toLowerCase().contains(player + " is already in the party")) {
                isWarpingUser = false;
                return;
            } else if(receivedMessage.toLowerCase().contains(player + " joined the party")) {
                playedJoined = true;
            }
        }

        if(SharedCommands.isWarpingOut) {
            if(receivedMessage.toLowerCase().contains(SharedCommands.player + " joined the party")) {
                SharedCommands.playedJoined = true;
            }
        }

        Matcher chatMatcher = chatPattern.matcher(receivedMessage);
        if(!chatMatcher.find()) return;
        String command = chatMatcher.group("command");
        String argument = chatMatcher.group("argument");
        String sender = chatMatcher.group("username");

        if(!NobaAddons.config.debugMode && (!commands.contains(command.toLowerCase()) || cooldown > 0)) return;
        cooldown = 3;
        startCooldown();
        switch(command.toLowerCase()) {
            case "help":
                if(NobaAddons.PLAYER_IGN.equals(sender) || !NobaAddons.config.dmHelpCommand) return;
                ChatUtils.delayedSend("pc NobaAddons > !help, !warpme , !(partyme|pme), !warpout");
                break;
            case "warpme":
                if(NobaAddons.PLAYER_IGN.equals(sender) || !NobaAddons.config.warpMeCommand) return;
                warpUserCommand(sender);
                break;
            case "partyme":
            case "pme":
                if(NobaAddons.PLAYER_IGN.equals(sender) || !NobaAddons.config.partyMeCommand) return;
                ChatUtils.delayedSend("p " + sender);
                break;
            case "warpout":
                if(NobaAddons.PLAYER_IGN.equals(sender) || !NobaAddons.config.dmWarpOutCommand) return;
                SharedCommands.warpOutCommand(argument, "msg " + sender);
                break;
            default:
                if(NobaAddons.config.debugMode) NobaAddons.LOGGER.warn("Unexpected value while parsing dm command: " + command.toLowerCase());
        }
    }

    public void warpUserCommand(String username) {
        if(isWarpingUser) {
            ChatUtils.sendCommand("r Warp-in is on cooldown, try again later!");
            return;
        }
        player = username.toLowerCase();
        ChatUtils.delayedSend("p " + player);
        isWarpingUser = true;
        warpUser();
    }

    private static class WarpInUser extends Thread {
        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            int secondsPassed = 0;
            while(isWarpingUser) {
                if(secondsPassed++ == 60) {
                    if(!playedJoined) {
                        ChatUtils.sendCommand("msg " + player + " Warp timed out, didn't join party.");
                        isWarpingUser = false;
                    }
                    break;
                }
                if(playedJoined) {
                    ChatUtils.sendCommand("p warp");
                    try {
                        sleep(1000);
                    } catch(InterruptedException ignored) {}
                    ChatUtils.sendCommand("p kick " + player);
                    playedJoined = false;
                    isWarpingUser = false;
                    break;
                }
                try {
                    sleep(1000);
                } catch(InterruptedException ignored) {}
            }
        }
    }

    public void warpUser() {
        WarpInUser thread = new WarpInUser();
        thread.setName("warp-user-" + player);
        thread.start();
    }

    private static class CommandsCooldown extends Thread {
        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            do {
                try {
                    sleep(1000);
                } catch(InterruptedException ignored) {}
            } while (--cooldown != 0);
        }
    }

    public void startCooldown() {
        CommandsCooldown thread = new CommandsCooldown();
        thread.setName("dm-command-cooldown");
        thread.start();
    }
}
