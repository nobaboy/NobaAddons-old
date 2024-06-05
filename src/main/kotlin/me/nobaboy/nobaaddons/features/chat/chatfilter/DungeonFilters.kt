package me.nobaboy.nobaaddons.features.chat.chatfilter

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.config.features.chat.chatfilter.ChatFilterConfig.MessageState
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.NumberUtils.formatDouble
import me.nobaboy.nobaaddons.util.RegexUtils.matchMatcher
import me.nobaboy.nobaaddons.util.StringUtils.startsWith
import net.minecraftforge.client.event.ClientChatReceivedEvent
import java.util.regex.Pattern

object DungeonFilters {
    private val config get() = NobaAddons.config.chat.chatFilter.dungeons

    private val pickupPattern: Pattern = Pattern.compile("A (?<item>[A-z ]+) was picked up!")
    private val playerObtainPattern: Pattern = Pattern.compile("(?:\\[[A-Z+]+] )?[A-z0-9_]+ has obtained (?<item>[A-z ]+)!")
    private val blacklistedItems = listOf(
        "Blood Key",
        "Wither Key"
    )

    private val blessingFindPattern: Pattern =
        Pattern.compile("^DUNGEON BUFF! ([A-z0-9_]+ found a|A) Blessing of (?<blessing>[A-z]+) [IV]+( was found)?!( \\([A-z0-9 ]+\\))?")
    private val blessingStatsPattern: Pattern =
        Pattern.compile("(?<one>\\+[\\d.]+x?)( & )?(?<two>\\+[\\d.]+x?)? (?<stat>❁ Strength|☠ Crit Damage|❈ Defense|❁ Damage|HP|❣ Health Regen|✦ Speed|✎ Intelligence)")

    private val customMessage = StringBuilder()
    private val blessingText = mutableListOf<String>()
    private var expectedBlessingLines: Int? = null

    private val healerOrbPickUpPattern: Pattern = Pattern.compile(
        "^◕ You picked up a (?<orb>[A-z ]+) from (?<player>[A-z0-9_]+) healing you for (?<health>[0-9,.]+)❤ and granting you (?<buff>[A-z0-9+% ]+) for 10 seconds\\."
    )

    private enum class Blessing(val text: String) {
        POWER("§c§lPOWER BUFF!§7"),
        WISDOM("§9§lWISDOM BUFF!§7"),
        STONE("§8§lSTONE BUFF!§7"),
        LIFE("§d§lLIFE BUFF!§7"),
        TIME("§2§lTIME BUFF!§7"),
    }

    fun processFilters(event: ClientChatReceivedEvent, message: String) {
        handleBlessingMessages(event, message)
        handleHealerOrbMessages(event, message)
        handlePickupOrObtainMessages(event, message)
    }

    private fun handleBlessingMessages(event: ClientChatReceivedEvent, message: String) {
        blessingFindPattern.matchMatcher(message) {
            if (!checkForMessageState(config.blessingsMessage.get(), event)) return

            val blessingType = group("blessing").uppercase()
            val blessingText = Blessing.valueOf(blessingType).text

            event.isCanceled = true
            expectedBlessingLines = if (blessingType == "POWER") 2 else 1
            customMessage.append(blessingText)
        }

        if (message.startsWith(listOf("     Granted you", "     Also granted you"))) {
            if (!checkForMessageState(config.blessingsMessage.get(), event, true)) return

            blessingText.add(message)
            if (blessingText.size != expectedBlessingLines) return

            buildAndSendBlessingMessage()
        }
    }

    private fun handleHealerOrbMessages(event: ClientChatReceivedEvent, message: String) {
        healerOrbPickUpPattern.matchMatcher(message) {
            if (!checkForMessageState(config.healerOrbMessage.get(), event, true)) return

            val orbType = group("orb")
            val player = group("player")
            val healthGained = group("health")
            val buff = group("buff")

            val healthMessage = if (healthGained.formatDouble() > 0.0) "§c+$healthGained❤ §7and " else ""
            val formattedMessage = "§e§lHEALER ORB! $healthMessage§a$buff§7 for picking up $player's $orbType."

            ChatUtils.addMessage(false, formattedMessage)
        }
    }

    private fun handlePickupOrObtainMessages(event: ClientChatReceivedEvent, message: String) {
        pickupPattern.matchMatcher(message) {
            if (!config.pickupObtainMessages) return

            val item = group("item")
            if (item !in blacklistedItems) event.isCanceled = true
        }

        playerObtainPattern.matchMatcher(message) {
            if (!config.pickupObtainMessages) return

            val item = group("item")
            if (item !in blacklistedItems) event.isCanceled = true
        }
    }

    private fun buildAndSendBlessingMessage() {
        var cycle = 0
        var previousFirst: String? = null
        var previousSecond: String? = null
        var previousStat: String? = null

        blessingText.forEach { line ->
            blessingStatsPattern.toRegex().findAll(line).forEach { match ->
                if (customMessage.isBlank()) return

                if (cycle++ > 0) customMessage.append(" and")

                val stat = match.groups["stat"]!!.value
                val first = match.groups["one"]!!.value
                val second = match.groups["two"]?.value

                if (previousFirst != first) {
                    previousFirst = first
                    customMessage.append(" $first")
                    if (second != null) customMessage.append(" &")
                }

                if (previousSecond != second && second != null) {
                    previousSecond = second
                    customMessage.append(" $second")
                }

                if (previousStat != stat) {
                    previousStat = stat
                    customMessage.append(" $stat")
                }
            }
        }

        ChatUtils.addMessage(false, customMessage.append(".").toString())
        customMessage.clear()
        blessingText.clear()
    }

    private fun checkForMessageState(
        messageState: MessageState,
        event: ClientChatReceivedEvent,
        cancelOnCompact: Boolean = false
    ): Boolean {
        return when (messageState) {
            MessageState.SHOWN -> false
            MessageState.COMPACT -> {
                if (cancelOnCompact) event.isCanceled = true
                true
            }
            MessageState.HIDDEN -> {
                event.isCanceled = true
                false
            }
        }
    }
}