package me.nobaboy.nobaaddons.features.chatcommands;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.HelpCommand;
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.WarpOutCommand;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildCommands extends ChatCommandManager {
    // .*? is for the player symbols added by mods
    private static final Pattern chatPattern = Pattern.compile("^Guild > .*?(?:\\[[A-Z+]+] )?(?<username>[A-z0-9_]+)(?<grank> \\[[A-z0-9 ]+])?.*?: !(?<command>[A-z0-9_]+) ?(?<argument>[A-z0-9_]+)?");

    {
        register(new HelpCommand(this, "gc", () -> NobaAddons.config.guildHelpCommand));
        register(new WarpOutCommand("gc", () -> NobaAddons.config.guildWarpOutCommand));
    }

    @Override
    protected Optional<Matcher> matchMessage(String message) {
        Matcher match = chatPattern.matcher(message);
        if(match.find()) {
            return Optional.of(match);
        }
        return Optional.empty();
    }


    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent event) {
        if(!NobaAddons.config.guildCommands) return;
        String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());

        if(WarpOutCommand.isWarpingOut) {
            if(receivedMessage.toLowerCase().contains(WarpOutCommand.player + " is already in the party")) {
                WarpOutCommand.isWarpingOut = false;
                return;
            } else if(receivedMessage.toLowerCase().contains(WarpOutCommand.player + " joined the party")) {
                WarpOutCommand.playerJoined = true;
                return;
            }
        }

        processMessage(receivedMessage);
    }
}
