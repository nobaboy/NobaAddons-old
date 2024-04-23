package me.nobaboy.nobaaddons.features.chatcommands.impl.shared;

import me.nobaboy.nobaaddons.features.chatcommands.ChatContext;
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand;
import me.nobaboy.nobaaddons.util.ChatUtils;

import java.util.function.Supplier;

public class WarpOutCommand implements IChatCommand {
    public static boolean isWarpingOut = false;
    public static boolean playerJoined = false;
    public static String player;
    private final String command;
    private final Supplier<Boolean> isEnabled;

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
            ChatUtils.delayedSend(command + " Warp out is on cooldown, try again later!");
        } else if(ctx.args().length == 0) {
            ChatUtils.delayedSend(command + " Please provide a username.");
        } else {
            player = ctx.args()[0];
            ChatUtils.delayedSend("p " + player);
            isWarpingOut = true;
            warpOutPlayer();
        }
    }

    @Override
    public boolean isEnabled() {
        return isEnabled.get();
    }

    public void warpOutPlayer() {
        WarpOutThread thread = new WarpOutThread();
        thread.setName("warp-out-" + player);
        thread.start();
    }

    private class WarpOutThread extends Thread {
        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            int secondsPassed = 0;
            while (true) {
                if(secondsPassed++ >= 60) {
                    if(!playerJoined) {
                        ChatUtils.delayedSend(command + " Warp out failed, " + player + " did not join party.");
                        isWarpingOut = false;
                    }
                    break;
                }
                if(playerJoined) {
                    ChatUtils.delayedSend("p warp");
                    try {
                        sleep(900);
                    } catch(InterruptedException ignored) {
                    }
                    ChatUtils.delayedSend("p disband");
                    try {
                        sleep(900);
                    } catch(InterruptedException ignored) {
                    }
                    ChatUtils.delayedSend(command + " Warp out successful.");
                    playerJoined = false;
                    isWarpingOut = false;
                    break;
                }
                try {
                    sleep(1000);
                } catch(InterruptedException ignored) {
                }
            }
        }
    }
}
