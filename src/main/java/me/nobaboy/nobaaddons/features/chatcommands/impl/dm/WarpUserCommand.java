package me.nobaboy.nobaaddons.features.chatcommands.impl.dm;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext;
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand;
import me.nobaboy.nobaaddons.util.ChatUtils;

public class WarpUserCommand implements IChatCommand {
    public static boolean isWarpingUser = false;
    public static boolean playerJoined = false;
    public static String player;

    @Override
    public String name() {
        return "warpme";
    }

    @Override
    public void run(ChatContext ctx) {
        if(isWarpingUser) {
            ChatUtils.sendCommand("r Warp-in is on cooldown, try again later!");
            return;
        }
        player = ctx.user().toLowerCase();
        ChatUtils.delayedCommand("p " + player);
        isWarpingUser = true;
        warpUser();
    }

    @Override
    public boolean isEnabled() {
        return NobaAddons.config.dmWarpMeCommand;
    }

    @SuppressWarnings("BusyWait")
    public void warpUser() {
        new Thread(() -> {
            int secondsPassed = 0;
            while(isWarpingUser) {
                if(secondsPassed++ == 60) {
                    if(!playerJoined) {
                        ChatUtils.sendCommand("msg " + player + " Warp timed out, didn't join party.");
                        isWarpingUser = false;
                    }
                    break;
                }
                if(playerJoined) {
                    ChatUtils.sendCommand("p warp");
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException ignored) { }
                    ChatUtils.sendCommand("p kick " + player);
                    playerJoined = false;
                    isWarpingUser = false;
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ignored) { }
            }
        }).start();
    }
}
