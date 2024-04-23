package me.nobaboy.nobaaddons.features.chatcommands.impl.party;

import com.google.common.collect.Lists;
import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext;
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.PartyUtils;

import java.util.List;

public class TransferCommand implements IChatCommand {
    @Override
    public String name() {
        return "transfer";
    }

    @Override
    public String usage() {
        return "(transfer|pt) [optional: username], !ptme";
    }

    @Override
    public void run(ChatContext ctx) {
        if(!PartyUtils.isLeader) return;
        if(!ctx.command().equalsIgnoreCase("ptme")) {
            String player = ctx.args().length == 0 ? ctx.user() : ctx.args()[0];
            if(player.equalsIgnoreCase(NobaAddons.getUsername())) return;
            ChatUtils.delayedSend("p transfer " + player);
            return;
        }
        if(ctx.user().equals(NobaAddons.getUsername())) return;
        ChatUtils.delayedSend("p transfer " + ctx.user());
    }

    @Override
    public boolean isEnabled() {
        return NobaAddons.config.partyTransferCommand;
    }

    @Override
    public List<String> aliases() {
        return Lists.newArrayList("ptme", "pt");
    }
}
