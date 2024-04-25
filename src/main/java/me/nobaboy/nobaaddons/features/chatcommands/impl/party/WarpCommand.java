package me.nobaboy.nobaaddons.features.chatcommands.impl.party;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext;
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand;
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.PartyUtils;
import org.apache.commons.lang3.StringUtils;

import static java.lang.Integer.parseInt;

public class WarpCommand implements IChatCommand {
    public static int delay = 0;
    public static boolean isWarping = false;
    public static boolean cancel = false;

    @Override
    public String name() {
        return "warp";
    }

    @Override
    public String usage() {
        return "warp [optional: seconds]";
    }

    @Override
    public void run(ChatContext ctx) {
        if(!PartyUtils.isLeader) return;
        warpCommand(ctx.args().length == 0 ? null : ctx.args()[0]);
    }

    @Override
    public boolean isEnabled() {
        return NobaAddons.config.partyWarpCommand;
    }

    public void warpCommand(String seconds) {
        if(seconds == null) {
            ChatUtils.delayedCommand("p warp");
        } else if(!StringUtils.isNumeric(seconds)) {
            ChatUtils.delayedCommand("pc First argument can either be an integer or empty");
        } else if(parseInt(seconds) > 15 || parseInt(seconds) < 3) {
            ChatUtils.delayedCommand("pc Warp delay has a maximum limit of 15 seconds and a minimum of 3.");
        } else {
            ChatUtils.delayedCommand("pc Warping in " + seconds + " (To cancel type '!cancel')");
            delay = parseInt(seconds);
            isWarping = true;
            startTimedWarp();
        }
    }

    @SuppressWarnings("BusyWait")
    public void startTimedWarp() {
        new Thread(() -> {
            int secondsPassed = --delay;
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ignored) { }
                if(cancel) {
                    ChatUtils.sendCommand("pc Warp cancelled...");
                    isWarping = false;
                    cancel = false;
                    break;
                }
                ChatUtils.sendCommand("pc " + secondsPassed);
                if(--secondsPassed == 0) {
                    ChatUtils.sendCommand("p warp");
                    isWarping = false;
                    break;
                }
            }
        }).start();
    }
}
