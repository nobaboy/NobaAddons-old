package me.nobaboy.nobaaddons.core

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
import me.nobaboy.nobaaddons.NobaAddons
import java.io.File

object Config : Vigilant(
    File("./config/nobaaddons/config.toml"),
    "NobaAddons ${NobaAddons.MOD_VERSION}",
    sortingBehavior = ConfigSorting
) {
    // region DM Commands

    @Property(
        type = PropertyType.SWITCH,
        name = "DM Commands",
        description = "Allows whoever DMs to use DM commands.\n/noba dmcommands",
        category = "Commands",
        subcategory = "DM Commands"
    )
    var dmCommands = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!help",
        description = "Sends all usable commands.",
        category = "Commands",
        subcategory = "DM Commands"
    )
    var dmHelpCommand = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!warpme",
        description = "Warp user to your lobby.",
        category = "Commands",
        subcategory = "DM Commands"
    )
    var dmWarpMeCommand = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!partyme or !pme",
        description = "Invite user to party.",
        category = "Commands",
        subcategory = "DM Commands"
    )
    var dmPartyMeCommand = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!warpout [username]",
        description = "Warp out a player.",
        category = "Commands",
        subcategory = "DM Commands"
    )
    var dmWarpOutCommand = false

    // endregion

    // region Party Commands

    @Property(
        type = PropertyType.SWITCH,
        name = "Party Commands",
        description = "Allows everyone in party to use party commands.\n/noba partycommands",
        category = "Commands",
        subcategory = "Party Commands"
    )
    var partyCommands = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!help",
        description = "Sends all usable commands.",
        category = "Commands",
        subcategory = "Party Commands"
    )
    var partyHelpCommand = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!transfer or !ptme",
        description = "Transfer party to the player who ran the command.",
        category = "Commands",
        subcategory = "Party Commands"
    )
    var partyTransferCommand = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!allinvite or !allinv",
        description = "Turns on all invite party setting.",
        category = "Commands",
        subcategory = "Party Commands"
    )
    var partyAllInviteCommand = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!warp [optional: seconds] and !cancel",
        description = "Requests party warp with an optional warp delay.",
        category = "Commands",
        subcategory = "Party Commands"
    )
    var partyWarpCommand = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!coords",
        description = "Sends current location of user.",
        category = "Commands",
        subcategory = "Party Commands"
    )
    var partyCoordsCommand = false

    // endregion

    // region Guild Commands

    @Property(
        type = PropertyType.SWITCH,
        name = "Guild Commands",
        description = "Allows everyone in guild to use guild commands.\n/noba guildcommands",
        category = "Commands",
        subcategory = "Guild Commands"
    )
    var guildCommands = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!help",
        description = "Sends all usable commands.",
        category = "Commands",
        subcategory = "Guild Commands"
    )
    var guildHelpCommand = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "!warpout [username]",
        description = "Warp out a player.",
        category = "Commands",
        subcategory = "Guild Commands"
    )
    var guildWarpOutCommand = false

    // endregion

    // region Notifiers

    @Property(
        type = PropertyType.SWITCH,
        name = "Cakes Eaten Notifier",
        description = "Notifies you once you've eaten all Century Cakes.",
        category = "Notifiers"
    )
    var cakesEatenNotifier = false

    @Property(
        type = PropertyType.NUMBER,
        name = "Current Number of Century Cakes",
        description = "Allows you to modify the number of century cakes when new ones are added.",
        category = "Notifiers",
        min = 1,
        max = 100
    )
    var centuryCakesAmount = 14

    @Property(
        type = PropertyType.SWITCH,
        name = "Totem of Corruption Notifier",
        description = "Notifies you once Totem of Corrupted expires.",
        category = "Notifiers"
    )
    var corruptionTotemNotifier = false


    // endregion
    // region Dungeons
    @Property(
        type = PropertyType.SWITCH,
        name = "Camp Blood After Time",
        description = "A timer that tell you to camp blood after a certain amount of time.",
        category = "Dungeons",
        subcategory = "QOL"
    )
    var bloodCampAfterTime = false

    @Property(
        type = PropertyType.SLIDER,
        name = "Time (in seconds)",
        description = "Time until blood camp warning.",
        category = "Dungeons",
        subcategory = "QOL",
        min = 1,
        max = 30
    )
    var timeTillBloodCamp = 30

    @Property(
        type = PropertyType.SWITCH,
        name = "Simon Says Timer",
        description = "Announces how much time it took to complete the Simon Says device.",
        category = "Dungeons",
        subcategory = "QOL"
    )
    var ssDeviceTimer = false

    @Property(
        type = PropertyType.SWITCH,
        name = "SS Time in Party Chat",
        description = "Instead of the message being client-side, send it in party chat.",
        category = "Dungeons",
        subcategory = "QOL"
    )
    var ssDeviceTimerPC = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Refill Pearls",
        description = "Run '/noba refillPearls' to refill your Enderpearls up to 16 or change the key bind in controls to your desired key.",
        category = "Dungeons",
        subcategory = "QOL"
    )
    var refillPearls = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Auto Refill Pearls",
        description = "Automatically refill your pearls on dungeon start instead of pressing a key bind or running a command",
        category = "Dungeons",
        subcategory = "UAYOR"
    )
    var autoRefillPearls = false

    // endregion

    // region Dev

    @Property(
        type = PropertyType.SWITCH,
        name = "Debug Mode",
        description = "§cYou should only turn this on if you know what you're doing, or if you were requested to turn this on.",
        category = "Dev"
    )
    var debugMode = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Slash from Messages",
        description = "§cRemoves the slash from the messages sent by the mod, only turn this on if you know what you're doing.",
        category = "Dev"
    )
    var removeSlash = false

    // endregion

    init {
        initialize()

        try {
            arrayOf(
                "dmHelpCommand",
                "dmWarpMeCommand",
                "dmPartyMeCommand",
                "dmWarpOutCommand"
            ).forEach { propertyName ->
                addDependency(propertyName, "dmCommands")
                registerListener(propertyName) { prop: Any ->
                    if (prop is Boolean && prop) dmCommands = true
                }
            }

            arrayOf(
                "partyHelpCommand",
                "partyTransferCommand",
                "partyAllInviteCommand",
                "partyWarpCommand",
                "partyCoordsCommand"
            ).forEach { propertyName ->
                addDependency(propertyName, "partyCommands")
                registerListener(propertyName) { prop: Any ->
                    if (prop is Boolean && prop) partyCommands = true
                }
            }

            arrayOf(
                "guildHelpCommand",
                "guildWarpOutCommand"
            ).forEach { propertyName ->
                addDependency(propertyName, "guildCommands")
                registerListener(propertyName) { prop: Any ->
                    if (prop is Boolean && prop) guildCommands = true
                }
            }

            // Notifiers
            addDependency("timeTillBloodCamp", "bloodCampAfterTime")
            addDependency("centuryCakesAmount", "cakesEatenNotifier")
            // Dungeons
            addDependency("ssDeviceTimerPC", "ssDeviceTimer")
            addDependency("autoRefillPearls", "refillPearls")

            markDirty()
        } catch (ex: Exception) {
            NobaAddons.LOGGER.error("Failed to add dependency in config", ex)
        }
    }

    private object ConfigSorting : SortingBehavior() {
        override fun getCategoryComparator(): Comparator<in Category> = Comparator { o1, o2 ->
            if (o1.name == "General") return@Comparator -1
            if (o2.name == "General") return@Comparator 1
            if (o1.name == "Dev" || o2.name == "Dev") return@Comparator 1
            else compareValuesBy(o1, o2) {
                it.name
            }
        }
    }
}