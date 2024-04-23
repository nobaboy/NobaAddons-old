package me.nobaboy.nobaaddons.features.chatcommands.impl.dm;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext;
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand;
import me.nobaboy.nobaaddons.util.ChatUtils;

import java.util.Collections;
import java.util.List;

public class PartyMeCommand implements IChatCommand {
    @Override
    public String name() {
        return "partyme";
    }

    @Override
    public void run(ChatContext ctx) {
        ChatUtils.delayedSend("p " + ctx.user());
    }

    @Override
    public boolean isEnabled() {
        return NobaAddons.config.dmPartyMeCommand;
    }

    @Override
    public List<String> aliases() {
        return Collections.singletonList("pme");
    }
}
