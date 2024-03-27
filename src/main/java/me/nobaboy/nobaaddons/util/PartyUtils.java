package me.nobaboy.nobaaddons.util;

import me.nobaboy.nobaaddons.NobaAddons;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartyUtils {
    final Pattern PARTY_INVITE = Pattern.compile("^(?:\\[[A-Z+]+] )?(?<leader>[A-z0-9_]+) invited (?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+) to the party! They have 60 seconds to accept.");
    final Pattern PARTY_JOIN = Pattern.compile("^You have joined (?:\\[[A-Z+]+] )?(?<leader>[A-z0-9_]+)'s party!");
    final Pattern PARTY_LEADER = Pattern.compile("^Party Leader: (?:\\[[A-Z+]+] )?(?<leader>[A-z0-9_]+) ●");
    final Pattern PARTY_LIST = Pattern.compile("-{50,53}|Party (?:Leader|Moderators|Members):?.*|You are not currently in a party\\.");

    final List<Pattern> LEADER_PATTERNS = Arrays.asList(
            Pattern.compile("^The party was transferred to (?:\\[[A-Z+]+] )?(?<leader>[A-z0-9_]+) by (?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+)"),
            Pattern.compile("^The party was transferred to (?:\\[[A-Z+]+] )?(?<leader>[A-z0-9_]+) because (?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+) left"),
            Pattern.compile("^(?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+) has promoted (?:\\[[A-Z+]+] )?(?<leader>[A-z0-9_]+) to Party Leader"),
            Pattern.compile("^(?:\\[[A-Z+]+] )?(?<leader>[A-z0-9_]+) invited (?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+) to the party! They have 60 seconds to accept.")
    );
    final List<Pattern> NO_PARTY_PATTERNS = Arrays.asList(
            Pattern.compile("^You left the party."),
            Pattern.compile("^You are not in a party right now."),
            Pattern.compile("^(?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+) has disbanded the party!"),
            Pattern.compile("^You have been kicked from the party by (?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+)"),
            Pattern.compile("^The party was disbanded because all invites expired and the party was empty.")
    );

    boolean gettingList = false;
    public static boolean isLeader = false;
    boolean inParty = false;
    String leaderName = null;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent ignored) {
        gettingList = false;
        getPartyList();
    }

    private void getPartyList() {
        if(Minecraft.getMinecraft().isSingleplayer() || gettingList || !Utils.onHypixel) return;
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch(InterruptedException ignored) {}
            gettingList = true;
            ChatUtils.sendCommand("party list");
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ignored) {}
            gettingList = false;
        }).start();
    }

    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent event) {
        String receivedMessage = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if(receivedMessage.startsWith("Party >")) inParty = true;
        if(gettingList) {
            Matcher listMatcher = PARTY_LIST.matcher(receivedMessage);
            if(listMatcher.find()) {
                event.setCanceled(true);
            }

            Matcher matcher = PARTY_LEADER.matcher(receivedMessage);
            if(matcher.find()) {
                leaderName = matcher.group("leader");
                if(leaderName.equals(NobaAddons.PLAYER_IGN)) isLeader = true;
                inParty = true;
                return;
            }
        }
        if(!inParty) {
            Matcher joinMatcher = PARTY_JOIN.matcher(receivedMessage);
            if(joinMatcher.find()) {
                leaderName = joinMatcher.group("leader");
                inParty = true;
            }

            Matcher inviteMatcher = PARTY_INVITE.matcher(receivedMessage);
            if(inviteMatcher.find()) {
                leaderName = inviteMatcher.group("leader");
                if(leaderName.equals(NobaAddons.PLAYER_IGN)) isLeader = true;
                inParty = true;
            }
        } else {
            boolean noParty = NO_PARTY_PATTERNS.stream()
                    .anyMatch(pattern -> pattern.matcher(receivedMessage).find());
            if(noParty) {
                inParty = false;
                isLeader = false;
                leaderName = null;
                return;
            }

            LEADER_PATTERNS.stream()
                    .map(pattern -> pattern.matcher(receivedMessage))
                    .filter(Matcher::find)
                    .findFirst()
                    .ifPresent(match -> {
                        leaderName = match.group("leader");
                        isLeader = leaderName.equals(NobaAddons.PLAYER_IGN);
                    });
        }
    }
}
