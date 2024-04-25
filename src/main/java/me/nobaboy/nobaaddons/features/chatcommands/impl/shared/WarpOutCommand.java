package me.nobaboy.nobaaddons.features.chatcommands.impl.shared;

import me.nobaboy.nobaaddons.features.chatcommands.ChatContext;
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand;
import me.nobaboy.nobaaddons.util.ChatUtils;

import java.util.function.Supplier;

public class WarpOutCommand implements IChatCommand {
    private final String command;
    private final Supplier<Boolean> isEnabled;

    public static boolean isWarpingOut = false;
    public static boolean playerJoined = false;
    public static String player;

    public WarpOutCommand(String command, Supplier<Boolean> enabled) {
        this.command = command;
        this.isEnabled = enabled;
    }

    @Override
    public String name() {
        return "warpout";
    }

    @Override
    public String usage() {
        return "warpout [username]";
    }

    @Override
    public void run(ChatContext ctx) {
        if(isWarpingOut) {
            ChatUtils.delayedCommand(command + " Warp out is on cooldown, try again later!");
        } else if(ctx.args().length == 0) {
            ChatUtils.delayedCommand(command + " Please provide a username.");
        } else {
            player = ctx.args()[0];
            ChatUtils.delayedCommand("p " + player);
            isWarpingOut = true;
            warpOutPlayer();
        }
    }

    @Override
    public boolean isEnabled() {
        return isEnabled.get();
    }

    private class WarpOutThread extends Thread {
        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            int secondsPassed = 0;
            while(true) {
                if(secondsPassed++ >= 60) {
                    if(!playerJoined) {
                        ChatUtils.delayedCommand(command + " Warp out failed, " + player + " did not join party.");
                        isWarpingOut = false;
                    }
                    break;
                }
                if(playerJoined) {
                    ChatUtils.delayedCommand("p warp");
                    try {
                        sleep(900);
                    } catch(InterruptedException ignored) { }
                    ChatUtils.delayedCommand("p disband");
                    try {
                        sleep(900);
                    } catch(InterruptedException ignored) { }
                    ChatUtils.delayedCommand(command + " Warp out successful.");
                    playerJoined = false;
                    isWarpingOut = false;
                    break;
                }
                try {
                    sleep(1000);
                } catch(InterruptedException ignored) { }
            }
        }
    }

    public void warpOutPlayer() {
        WarpOutThread thread = new WarpOutThread();
        thread.setName("warp-out-" + player);
        thread.start();
    }
}
