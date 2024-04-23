package me.nobaboy.nobaaddons.features.chatcommands;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.chatcommands.impl.dm.PartyMeCommand;
import me.nobaboy.nobaaddons.features.chatcommands.impl.dm.WarpUserCommand;
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.HelpCommand;
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.WarpOutCommand;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DMCommands extends ChatCommandManager {
    private static final Pattern chatPattern = Pattern.compile("^From (?:\\[[A-Z+]+] )?(?<username>[A-z0-9_]+): [!?.](?<command>[A-z0-9_]+) ?(?<argument>[A-z0-9_]+)?");

    {
        register(new HelpCommand(this, "r", () -> NobaAddons.config.dmHelpCommand));
        register(new PartyMeCommand());
        register(new WarpUserCommand());
        register(new WarpOutCommand("r", () -> NobaAddons.config.dmWarpOutCommand));
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
        if(!NobaAddons.config.dmCommands) return;
        String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());

        // there probably is a better way than wtf I'm doing here
        if(WarpUserCommand.isWarpingUser) {
            if(receivedMessage.toLowerCase().contains(WarpUserCommand.player + " is already in the party")) {
                WarpUserCommand.isWarpingUser = false;
                return;
            } else if(receivedMessage.toLowerCase().contains(WarpUserCommand.player + " joined the party")) {
                WarpUserCommand.playerJoined = true;
                return;
            }
        }

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
