package me.nobaboy.nobaaddons.features.chatcommands.impl.party;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext;
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand;

public class CancelCommand implements IChatCommand {
    @Override
    public String name() {
        return "cancel";
    }

    @Override
    public void run(ChatContext ctx) {
        WarpCommand.cancel = true;
    }

    @Override
    public boolean isEnabled() {
        return NobaAddons.config.partyWarpCommand;
    }
}
