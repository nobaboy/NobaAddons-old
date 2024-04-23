package me.nobaboy.nobaaddons.features.chatcommands.impl.party;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext;
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand;
import me.nobaboy.nobaaddons.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class CoordsCommand implements IChatCommand {
    @Override
    public String name() {
        return "coords";
    }

    @Override
    public void run(ChatContext ctx) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        ChatUtils.delayedSend("pc x: " + (int) player.posX + ", y: " + (int) player.posY + ", z: " + (int) player.posZ);
    }

    @Override
    public boolean isEnabled() {
        return NobaAddons.config.partyCoordsCommand;
    }
}
