package me.nobaboy.nobaaddons.features.chatcommands;

import com.google.common.collect.Lists;
import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.CooldownManager;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildCommands extends CooldownManager {
    List<String> commands = Lists.newArrayList("help", "warpout");
    Pattern chatPattern = Pattern.compile("^Guild > (?:\\[[A-Z+]+] )?(?<username>[A-z0-9_]+)(?<grank> \\[[A-z0-9 ]+])?: !(?<command>[A-z0-9_]+) ?(?<argument>[A-z0-9_]+)?");

    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent event) {
        if(!NobaAddons.config.guildCommands) return;
        String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());

        if(SharedCommands.isWarpingOut) {
            if(receivedMessage.toLowerCase().contains(SharedCommands.player + " joined the party")) {
                SharedCommands.playedJoined = true;
            }
        }

        Matcher chatMatcher = chatPattern.matcher(receivedMessage);
        if(!chatMatcher.find()) return;
        String command = chatMatcher.group("command");
        String argument = chatMatcher.group("argument");
//        String sender = chatMatcher.group("username");

        if(!NobaAddons.config.debugMode && (!commands.contains(command.toLowerCase()) || isOnCooldown())) return;
        startCooldown();
        switch(command.toLowerCase()) {
            case "help":
                if(!NobaAddons.config.guildHelpCommand) return;
                ChatUtils.delayedSend("gc NobaAddons > !help, !warpout");
                break;
            case "warpout":
                if(!NobaAddons.config.guildWarpOutCommand) return;
                SharedCommands.warpOutCommand(argument, "gc");
                break;
            default:
                if(NobaAddons.config.debugMode) NobaAddons.LOGGER.warn("Unexpected value while parsing guild command: " + command.toLowerCase());
        }
    }
}
