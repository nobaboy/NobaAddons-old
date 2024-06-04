package me.nobaboy.nobaaddons.api

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.HypixelCommands
import me.nobaboy.nobaaddons.util.RegexUtils.matchMatcher
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import me.nobaboy.nobaaddons.util.Utils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent
import java.util.regex.Pattern
import kotlin.time.Duration.Companion.seconds

// Util: (?:\[[A-Z+]+] )?(?<player>[A-z0-9_]+) -> Rank PlayerName
object PartyAPI {
    // User Patterns
    private val userPartyJoinPattern : Pattern =
        Pattern.compile("^You have joined (?:\\[[A-Z+]+] )?(?<leader>[A-z0-9_]+)'s party!")
    private val userKickedPattern : Pattern =
        Pattern.compile("^You have been kicked from the party by (?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+)")

    // Others Patterns
    private val otherPartyJoinPattern : Pattern = Pattern.compile("^(?:\\[A-Z+]+] )?(?<name>[A-z0-9_]+) joined the party\\.")
    private val othersInPartyPattern : Pattern = Pattern.compile("^You'll be partying with: (?<names>.*)")
    private val otherLeftPartyPattern : Pattern = Pattern.compile("^(?:\\[[A-Z+]+] )?(?<name>[A-z0-9_]+) left the party\\.")
    private val otherKickedPattern : Pattern =
        Pattern.compile("^(?:\\[[A-Z+]+] )?(?<name>[A-z0-9_]+) has been removed from the party\\.")
    private val otherKickedOfflinePattern : Pattern =
        Pattern.compile("^Kicked (?:\\[[A-Z+]+] )?(?<name>[A-z0-9_]+) because they were offline\\.")
    private val otherDisconnectPattern : Pattern =
        Pattern.compile("^(?:\\[[A-Z+]+] )?(?<name>[A-z0-9_]+) was removed from your party because they disconnected\\.")

    // Party Finder
    private val kuudraQueuePattern : Pattern =
        Pattern.compile("^Party Finder > (?<name>[A-z0-9_]+) joined the group! \\([A-z0-9 ]+\\)")
    private val dungeonQueuePattern : Pattern =
        Pattern.compile("^Party Finder > (?<name>[A-z0-9_]+) joined the dungeon group! \\([A-z0-9 ]+\\)")

    // Party General
    private val partyMembersListPattern : Pattern = Pattern.compile("^Party (?<type>Leader|Moderators|Members): (?<names>.*)")
    private val transferByPlayerPattern : Pattern =
        Pattern.compile("^The party was transferred to (?:\\[[A-Z+]+] )?(?<newLeader>[A-z0-9_]+) by (?:\\[[A-Z+]+] )?(?<formerLeader>[A-z0-9_]+)")
    private val transferOnLeavePattern : Pattern =
        Pattern.compile("^The party was transferred to (?:\\[[A-Z+]+] )?(?<newLeader>[A-z0-9_]+) because (?:\\[[A-Z+]+] )?(?<formerLeader>[A-z0-9_]+) left")
    private val partyInvitePattern : Pattern =
        Pattern.compile("^(?:\\[[A-Z+]+] )?(?<leader>[A-z0-9_]+) invited (?:\\[[A-Z+]+] )?[A-z0-9_]+ to the party! They have 60 seconds to accept.")
    private val partyDisbandPattern : Pattern =
        Pattern.compile("^(?:\\[[A-Z+]+] )?(?<former>[A-z0-9_]+) has disbanded the party!")

    // Party Misc
    private val partyListPattern : Pattern =
        Pattern.compile("^-{53}|^Party Members \\([0-9]+\\)|^Party (?<type>Leader|Moderators|Members): (?<names>.*)|^You are not currently in a party\\.")
    private val partyChatPattern : Pattern = Pattern.compile("^Party > (?:\\[[A-Z+]+ )?(?<name>[A-z0-9_]+): .*")

    private var storedPartyList = mutableListOf<String>()
    private var gettingList = false
    private var task: Job? = null

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

    private fun getPartyList() {
        task = NobaAddons.coroutineScope.launch {
            delay(5.seconds)
            if (!Utils.onHypixel()) return@launch
            if (NobaAddons.config.dev.debugMode) NobaAddons.LOGGER.info("Getting party list...")

            gettingList = true
            HypixelCommands.partyList()
            delay(1.seconds)
            gettingList = false
        }
    }

    @SubscribeEvent
    fun onConnectToServer(ignored: ClientConnectedToServerEvent) {
        gettingList = false
        getPartyList()
    }

    @SubscribeEvent
    fun onDisconnectFromServer(ignored: ClientDisconnectionFromServerEvent) {
        storedPartyList.clear()
        task?.cancel()
    }

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        val receivedMessage = event.message.unformattedText.cleanString()

        if (gettingList) {
            partyListPattern.matchMatcher(receivedMessage) {
                storedPartyList.add(receivedMessage)
                event.isCanceled = true
            }

            storedPartyList.forEach { line ->
                partyMembersListPattern.matchMatcher(line) {
                    inParty = true

                    val type = group("type")
                    val isPartyLeader = type == "Leader"
                    val names = group("names")

                    addPlayersToList(isPartyLeader, names)
                }
            }
            return
        }

        partyChatPattern.matchMatcher(receivedMessage) {
            val name = group("name")
            addPlayer(name)
        }

        // Misc
        partyInvitePattern.matchMatcher(receivedMessage) {
            val name = group("leader")
            partyLeader = name
            addPlayer(name)
        }

        // Member Join
        userPartyJoinPattern.matchMatcher(receivedMessage) {
            val name = group("leader")
            partyLeader = name
            addPlayer(name)
        }
        otherPartyJoinPattern.matchMatcher(receivedMessage) {
            val name = group("name")
            if (partyMembers.size == 1) {
                partyLeader = Utils.getPlayerName()
            }
            addPlayer(name)
        }
        kuudraQueuePattern.matchMatcher(receivedMessage) {
            val name = group("name")
            addPlayer(name)
        }
        dungeonQueuePattern.matchMatcher(receivedMessage) {
            val name = group("name")
            addPlayer(name)
        }

        // Member Leave
        otherLeftPartyPattern.matchMatcher(receivedMessage) {
            val name = group("name")
            removePlayer(name)
        }
        otherKickedPattern.matchMatcher(receivedMessage) {
            val name = group("name")
            removePlayer(name)
        }
        otherKickedOfflinePattern.matchMatcher(receivedMessage) {
            val name = group("name")
            removePlayer(name)
        }
        otherDisconnectPattern.matchMatcher(receivedMessage) {
            val name = group("name")
            removePlayer(name)
        }
        transferOnLeavePattern.matchMatcher(receivedMessage) {
            val formerLeader = group("formerLeader")
            partyLeader = group("newLeader")
            partyMembers.remove(formerLeader)
            inParty = true
        }
        transferByPlayerPattern.matchMatcher(receivedMessage) {
            partyLeader = group("newLeader")
            inParty = true
        }

        // Party Disband
        partyDisbandPattern.matchMatcher(receivedMessage) {
            partyLeft()
        }
        userKickedPattern.matchMatcher(receivedMessage) {
            partyLeft()
        }
        if (receivedMessage == "You left the party." ||
            receivedMessage == "The party was disbanded because all invites expired and the party was empty." ||
            receivedMessage == "You are not currently in a party."
        ) {
            partyLeft()
        }

        // Party Members
        othersInPartyPattern.matchMatcher(receivedMessage) {
            for (name in group("names").replace(Regex("\\[[A-Z+]+] "), "").split(", ")) {
                addPlayer(name)
            }
        }

        // Party List
        partyMembersListPattern.matchMatcher(receivedMessage) {
            inParty = true

            val type = group("type")
            val isPartyLeader = type == "Leader"
            val names = group("names")

            addPlayersToList(isPartyLeader, names)
        }
    }

    private fun addPlayersToList(isPartyLeader: Boolean, names: String) {
        inParty = true

        for (name in names.split(" ● ")) {
            val playerName = name.replace(" ●", "")
                .replace(Regex("\\[[A-Z+]+] "), "")
                .split(" ")

            playerName.forEach {
                if (it == "") return

                addPlayer(it)
                if (isPartyLeader) {
                    partyLeader = it
                }
            }
        }
    }

    private fun addPlayer(playerName: String) {
        if (partyMembers.contains(playerName)) return
        partyMembers.add(playerName)
        inParty = true
    }

    private fun removePlayer(playerName: String) {
        partyMembers.remove(playerName)
    }

    private fun partyLeft() {
        partyMembers.clear()
        partyLeader = null
        inParty = false
    }

    fun isLeader() = partyLeader == Utils.getPlayerName()
}