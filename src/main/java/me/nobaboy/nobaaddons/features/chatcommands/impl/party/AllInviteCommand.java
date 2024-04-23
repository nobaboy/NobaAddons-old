package me.nobaboy.nobaaddons.features.chatcommands.impl.party;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext;
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.PartyUtils;

import java.util.Collections;
import java.util.List;

public class AllInviteCommand implements IChatCommand {
    @Override
    public String name() {
        return "allinvite";
    }

    @Override
    public String usage() {
        return "(allinvite|allinv)";
    }

    @Override
    public void run(ChatContext ctx) {
        if(!PartyUtils.isLeader) return;
        ChatUtils.delayedSend("p settings allinvite");
    }

    @Override
    public boolean isEnabled() {
        return NobaAddons.config.partyAllInviteCommand;
    }

    @Override
    public List<String> aliases() {
        return Collections.singletonList("allinv");
    }
}
