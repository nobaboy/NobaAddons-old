package me.nobaboy.nobaaddons.api

import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.StringUtils.cleanMessage
import me.nobaboy.nobaaddons.util.StringUtils.matchMatcher
import me.nobaboy.nobaaddons.util.Utils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Pattern

// Util: (?:\[[A-Z+]+] )?(?<player>[A-z0-9_]+) -> Rank PlayerName
object PartyAPI {
    // User Patterns
    private val userPartyJoin: Pattern =
        Pattern.compile("^You have joined (?:\\[[A-Z+]+] )?(?<leader>[A-z0-9_]+)'s party!")
    private val userKicked: Pattern =
        Pattern.compile("^You have been kicked from the party by (?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+)")

    // Others Patterns
    private val otherPartyJoin: Pattern = Pattern.compile("^(?:\\[A-Z+]+] )?(?<name>[A-z0-9_]+) joined the party\\.")
    private val othersInParty: Pattern = Pattern.compile("^You'll be partying with: (?<names>.*)")
    private val otherLeftParty: Pattern = Pattern.compile("^(?:\\[[A-Z+]+] )?(?<name>[A-z0-9_]+) left the party\\.")
    private val otherKicked: Pattern =
        Pattern.compile("^(?:\\[[A-Z+]+] )?(?<name>[A-z0-9_]+) has been removed from the party\\.")
    private val otherKickedOffline: Pattern =
        Pattern.compile("^Kicked (?:\\[[A-Z+]+] )?(?<name>[A-z0-9_]+) because they were offline\\.")
    private val otherDisconnect: Pattern =
        Pattern.compile("^(?:\\[[A-Z+]+] )?(?<name>[A-z0-9_]+) was removed from your party because they disconnected\\.")

    // Party Finder
    private val kuudraQueue: Pattern =
        Pattern.compile("^Party Finder > (?<name>[A-z0-9_]+) joined the group! \\([A-z0-9 ]+\\)")
    private val dungeonQueue: Pattern =
        Pattern.compile("^Party Finder > (?<name>[A-z0-9_]+) joined the dungeon group! \\([A-z0-9 ]+\\)")

    // Party General
    private val partyMembersList: Pattern = Pattern.compile("^Party (?<type>Leader|Moderators|Members): (?<names>.*)")
    private val transferByPlayer: Pattern =
        Pattern.compile("^The party was transferred to (?:\\[[A-Z+]+] )?(?<newLeader>[A-z0-9_]+) by (?:\\[[A-Z+]+] )?(?<formerLeader>[A-z0-9_]+)")
    private val transferOnLeave: Pattern =
        Pattern.compile("^The party was transferred to (?:\\[[A-Z+]+] )?(?<newLeader>[A-z0-9_]+) because (?:\\[[A-Z+]+] )?(?<formerLeader>[A-z0-9_]+) left")
    private val partyDisband: Pattern =
        Pattern.compile("^(?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+) has disbanded the party!")

    private val partyChat: Pattern = Pattern.compile("^Party > (?:\\[[A-Z+]+ )?(?<name>[A-z0-9_]+): .*")

    var inParty: Boolean = false
    var partyMembers = mutableListOf<String>()
    var partyLeader: String? = null

    fun listMembers() {
        val partySize = partyMembers.size
        if (partySize == 0) {
            ChatUtils.addMessage("Party seems to empty...")
            return
        }
        ChatUtils.addMessage("Party Members ($partySize):")
        for (member in partyMembers) {
            val isMemberLeader = if (partyLeader.equals(member)) "§9(Leader)" else ""
            ChatUtils.addMessage(false, " §b- §7$member $isMemberLeader")
        }
    }

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        val receivedMessage = event.message.unformattedText.cleanMessage()

        partyChat.matchMatcher(receivedMessage) {
            val name = group("name")
            addPlayer(name)
        }

        // Member Join
        userPartyJoin.matchMatcher(receivedMessage) {
            val name = group("leader")
            partyLeader = name
            inParty = true
            addPlayer(name)
        }
        otherPartyJoin.matchMatcher(receivedMessage) {
            val name = group("name")
            if (partyMembers.size == 1) {
                partyLeader = Utils.getPlayerName()
            }
            inParty = true
            addPlayer(name)
        }
        kuudraQueue.matchMatcher(receivedMessage) {
            val name = group("name")
            inParty = true
            addPlayer(name)
        }
        dungeonQueue.matchMatcher(receivedMessage) {
            val name = group("name")
            inParty = true
            addPlayer(name)
        }

        // Member Leave
        otherLeftParty.matchMatcher(receivedMessage) {
            val name = group("name")
            inParty = true
            addPlayer(name)
        }
        otherKicked.matchMatcher(receivedMessage) {
            val name = group("name")
            inParty = true
            addPlayer(name)
        }
        otherKickedOffline.matchMatcher(receivedMessage) {
            val name = group("name")
            inParty = true
            addPlayer(name)
        }
        otherDisconnect.matchMatcher(receivedMessage) {
            val name = group("name")
            inParty = true
            addPlayer(name)
        }
        transferOnLeave.matchMatcher(receivedMessage) {
            val formerLeader = group("formerLeader")
            partyLeader = group("newLeader")
            partyMembers.remove(formerLeader)
            inParty = true
        }
        transferByPlayer.matchMatcher(receivedMessage) {
            partyLeader = group("newLeader")
            inParty = true
        }

        // Party Disband
        partyDisband.matchMatcher(receivedMessage) {
            partyLeft()
        }
        userKicked.matchMatcher(receivedMessage) {
            partyLeft()
        }
        if (receivedMessage == "You left the party." ||
            receivedMessage == "The party was disbanded because all invites expired and the party was empty." ||
            receivedMessage == "You are not currently in a party."
        ) {
            partyLeft()
        }

        // Party Members
        othersInParty.matchMatcher(receivedMessage) {
            for (name in group("names").replace(Regex("\\[[A-Z+]+] "), "").split(", ")) {
                addPlayer(name)
            }
        }

        // Party List
        partyMembersList.matchMatcher(receivedMessage) {
            inParty = true

            val type = group("type")
            val isPartyLeader = type == "Leader"
            val names = group("names")

            for (name in names.split(" ● ")) {
                println("`$name`")

                val playerName = name.replace(" ●", "")
                    .replace(Regex("\\[[A-Z+]+] "), "")
                    .split(" ")

                println(playerName)

                playerName.forEach {
                    if (it == "") return

                    addPlayer(it)
                    if (isPartyLeader) {
                        partyLeader = it
                    }
                }
            }
        }
    }

    private fun addPlayer(playerName: String) {
        if (partyMembers.contains(playerName)) return
        partyMembers.add(playerName)
    }

    private fun partyLeft() {
        partyMembers.clear()
        partyLeader = null
        inParty = false
    }
}