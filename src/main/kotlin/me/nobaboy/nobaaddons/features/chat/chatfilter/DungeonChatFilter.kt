package me.nobaboy.nobaaddons.features.chat.chatfilter

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.config.features.chat.chatfilter.ChatFilterConfig.MessageState
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.NumberUtils.formatDouble
import me.nobaboy.nobaaddons.util.RegexUtils.matchMatcher
import me.nobaboy.nobaaddons.util.StringUtils.startsWith
import net.minecraftforge.client.event.ClientChatReceivedEvent
import java.util.regex.Pattern

object DungeonChatFilter {
    private val config get() = NobaAddons.config.chat.chatFilter.dungeons

    data class BlessingDetail(val stat: String, val firstBuff: String, val secondBuff: String?)

    private val itemPickupPattern: Pattern = Pattern.compile("A (?<item>[A-z ]+) was picked up!")
    private val playerObtainPattern: Pattern = Pattern.compile("(?:\\[[A-Z+]+] )?[A-z0-9_]+ has obtained (?<item>[A-z ]+)!")

    private val blessingFindPattern: Pattern = Pattern.compile(
        "^DUNGEON BUFF! ([A-z0-9_]+ found a|A) Blessing of (?<blessing>[A-z]+) [IV]+( was found)?!( \\([A-z0-9 ]+\\))?"
    )
    private val blessingStatsPattern: Pattern = Pattern.compile(
        "(?<one>\\+[\\d.]+x?)( & )?(?<two>\\+[\\d.]+x?)? (?<stat>❁ Strength|☠ Crit Damage|❈ Defense|❁ Damage|HP|❣ Health Regen|✦ Speed|✎ Intelligence)"
    )

    private val otherPickupUserOrbPattern: Pattern = Pattern.compile("◕ [A-z0-9_]+ picked up your (?<orb>[A-z ]+)!")
    private val userPickupHealerOrbPattern: Pattern = Pattern.compile(
        "^◕ You picked up a (?<orb>[A-z ]+) from (?<player>[A-z0-9_]+) healing you for (?<health>[0-9,.]+)❤ and granting you (?<buff>[A-z0-9+% ]+) for 10 seconds\\."
    )

    private val ignoredItems = listOf("Superboom TNT", "Revive Stone", "Beating Heart", "Vitamin Death", "Optical Lens")
    private val allowedItems = listOf("Wither Key", "Blood Key")

    private val messageBuilder = StringBuilder()
    private val blessingMessageLines = mutableListOf<String>()
    private var expectedBlessingLines: Int? = null

    private enum class BlessingCategory(val text: String) {
        POWER("§c§lPOWER BUFF!§7"),
        WISDOM("§9§lWISDOM BUFF!§7"),
        STONE("§8§lSTONE BUFF!§7"),
        LIFE("§d§lLIFE BUFF!§7"),
        TIME("§2§lTIME BUFF!§7"),
    }

    fun processFilters(event: ClientChatReceivedEvent, message: String) {
        processBlessingMessages(event, message)
        processHealerOrbMessages(event, message)
        processPickupOrObtainMessages(event, message)
    }

    private fun processBlessingMessages(event: ClientChatReceivedEvent, message: String) {
        blessingFindPattern.matchMatcher(message) {
            if (!shouldProcessMessage(config.blessingsMessage.get(), event)) return

            val blessingType = group("blessing").uppercase()
            val blessingTypeText = BlessingCategory.valueOf(blessingType).text

            expectedBlessingLines = if (blessingType == "POWER") 2 else 1
            messageBuilder.append(blessingTypeText)
        }

        if (message.startsWith(listOf("     Granted you", "     Also granted you"))) {
            if (!shouldProcessMessage(config.blessingsMessage.get(), event)) return

            blessingMessageLines.add(message)
            if (blessingMessageLines.size == expectedBlessingLines) {
                compileAndSendBlessingMessage()
            }
        }
    }

    private fun processHealerOrbMessages(event: ClientChatReceivedEvent, message: String) {
        userPickupHealerOrbPattern.matchMatcher(message) {
            if (!shouldProcessMessage(config.healerOrbMessage.get(), event)) return

            val orbType = group("orb")
            val playerName = group("player")
            val healthAmount = group("health")
            val buffDetail = group("buff")

            val healthMessage = if (healthAmount.formatDouble() > 0.0) "§c+$healthAmount❤ §7and " else ""
            val formattedMessage = "§e§lHEALER ORB! $healthMessage§a$buffDetail§7 for picking up $playerName's $orbType."

            ChatUtils.addMessage(false, formattedMessage)
        }

        otherPickupUserOrbPattern.matchMatcher(message) { event.isCanceled = true }
    }

    private fun processPickupOrObtainMessages(event: ClientChatReceivedEvent, message: String) {
        fun cancelIfNotIgnored(item: String) {
            if (item in allowedItems) event.isCanceled = true
        }

        itemPickupPattern.matchMatcher(message) {
            if (config.pickupObtainMessages) cancelIfNotIgnored(group("item"))
        }

        playerObtainPattern.matchMatcher(message) {
            if (!config.pickupObtainMessages) {
                val item = group("item")

                if (config.allow5050ItemMessage) {
                    if (item in ignoredItems && item !in allowedItems) event.isCanceled = true
                } else {
                    if (item in ignoredItems) event.isCanceled = true
                }
            }
        }
    }

    private fun compileAndSendBlessingMessage() {
        val blessingDetails = blessingMessageLines.flatMap { line ->
            blessingStatsPattern.toRegex().findAll(line).map { match ->
                val stat = match.groups["stat"]!!.value
                val first = match.groups["one"]!!.value
                val second = match.groups["two"]?.value
                BlessingDetail(stat, first, second)
            }
        }

        var previousStat: String? = null
        var previousFirst: String? = null
        var previousSecond: String? = null

        blessingDetails.forEachIndexed { index, detail ->
            if (previousFirst != detail.firstBuff) {
                previousFirst = detail.firstBuff
                messageBuilder.append(" ${detail.firstBuff}")
                if (detail.secondBuff != null) messageBuilder.append(" &")
            }
            if (previousSecond != detail.secondBuff && detail.secondBuff != null) {
                previousSecond = detail.secondBuff
                messageBuilder.append(" ${detail.secondBuff}")
            }
            if (previousStat != detail.stat) {
                previousStat = detail.stat
                messageBuilder.append(" ${detail.stat}")
            }
            messageBuilder.append(when (blessingDetails.size - index) {
                1 -> "."
                2 -> " and"
                else -> ","
            })
        }

        ChatUtils.addMessage(false, messageBuilder.toString())
        messageBuilder.clear()
        blessingMessageLines.clear()
    }

    private fun shouldProcessMessage(
        messageState: MessageState,
        event: ClientChatReceivedEvent,
        cancelIfCompact: Boolean = true
    ): Boolean {
        return when (messageState) {
            MessageState.SHOWN -> false
            MessageState.COMPACT -> {
                if (cancelIfCompact) event.isCanceled = true
                true
            }
            MessageState.HIDDEN -> {
                event.isCanceled = true
                false
            }
        }
    }
}