package me.nobaboy.nobaaddons.features.chatcommands;

import me.nobaboy.nobaaddons.util.ChatUtils;

public class SharedCommands {
    static boolean isWarpingOut = false;
    static boolean playedJoined = false;
    static String player;
    static String _sender;

    public static void warpOutCommand(String username, String sender) {
        if(isWarpingOut) {
            ChatUtils.delayedSend(sender + " Warp out is on cooldown, try again later!");
        } else if(username == null) {
            ChatUtils.delayedSend(sender + " Please provide a username.");
        } else {
            ChatUtils.delayedSend("p " + username);
            player = username;
            _sender = sender;
            isWarpingOut = true;
            warpOutPlayer();
        }
    }

    private static class WarpOutThread extends Thread {
        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            int secondsPassed = 0;
            while(true) {
                if(secondsPassed++ >= 60) {
                    if(!playedJoined) {
                        ChatUtils.delayedSend(_sender + " Warp out failed, " + player + " did not join party.");
                        isWarpingOut = false;
                    }
                    break;
                }
                if(playedJoined) {
                    ChatUtils.delayedSend("p warp");
                    try {
                        sleep(900);
                    } catch(InterruptedException ignored) {}
                    ChatUtils.delayedSend("p disband");
                    try {
                        sleep(900);
                    } catch(InterruptedException ignored) {}
                    ChatUtils.delayedSend(_sender + " Warp out successful.");
                    playedJoined = false;
                    isWarpingOut = false;
                    break;
                }
                try {
                    sleep(1000);
                } catch(InterruptedException ignored) {}
            }
        }
    }

    public static void warpOutPlayer() {
        WarpOutThread thread = new WarpOutThread();
        thread.setName("warp-out-" + player);
        thread.start();
    }
}
