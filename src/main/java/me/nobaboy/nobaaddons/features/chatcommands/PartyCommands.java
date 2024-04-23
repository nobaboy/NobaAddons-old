package me.nobaboy.nobaaddons.features.chatcommands;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.chatcommands.impl.party.*;
import me.nobaboy.nobaaddons.features.chatcommands.impl.shared.HelpCommand;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartyCommands extends ChatCommandManager {
    // .*? is for the player symbols added by mods
    private static final Pattern chatPattern = Pattern.compile("^Party > .*?(?:\\[[A-Z+]+] )?(?<username>[A-z0-9_]+).*?: !(?<command>[A-z0-9_]+) ?(?<argument>[A-z0-9_]+)?");

    {
        register(new HelpCommand(this, "pc", () -> NobaAddons.config.partyHelpCommand));
        register(new TransferCommand());
        register(new AllInviteCommand());
        register(new WarpCommand());
        register(new CancelCommand());
        register(new CoordsCommand());
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
        if(!NobaAddons.config.partyCommands) return;
        String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());

        processMessage(receivedMessage);
    }
}