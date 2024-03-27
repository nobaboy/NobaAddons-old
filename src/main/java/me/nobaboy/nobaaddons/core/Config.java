package me.nobaboy.nobaaddons.core;

import me.nobaboy.nobaaddons.NobaAddons;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class Config extends Vigilant {
    // DM Commands

    @Property(
            type = PropertyType.SWITCH,
            name = "DM Commands",
            description = "Allows whoever DMs to use DM commands.\n/noba dmcommands",
            category = "Commands",
            subcategory = "DM Commands"
    )
    public boolean dmCommands = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!help",
            description = "Sends all usable commands.",
            category = "Commands",
            subcategory = "DM Commands"
    )
    public boolean dmHelpCommand = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!warpme",
            description = "Warp user to your lobby.",
            category = "Commands",
            subcategory = "DM Commands"
    )
    public boolean warpMeCommand = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!partyme or !pme",
            description = "Invite user to party.",
            category = "Commands",
            subcategory = "DM Commands"
    )
    public boolean partyMeCommand = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!warpout [username]",
            description = "Warp out a player.",
            category = "Commands",
            subcategory = "DM Commands"
    )
    public boolean dmWarpOutCommand = false;

    // Party Commands

    @Property(
            type = PropertyType.SWITCH,
            name = "Party Commands",
            description = "Allows everyone in party to use party commands.\n/noba partycommands",
            category = "Commands",
            subcategory = "Party Commands"
    )
    public boolean partyCommands = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!help",
            description = "Sends all usable commands.",
            category = "Commands",
            subcategory = "Party Commands"
    )
    public boolean partyHelpCommand = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!transfer or !ptme",
            description = "Transfer party to the player who ran the command.",
            category = "Commands",
            subcategory = "Party Commands"
    )
    public boolean transferCommand = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!allinvite or !allinv",
            description = "Turns on all invite party setting.",
            category = "Commands",
            subcategory = "Party Commands"
    )
    public boolean allInviteCommand = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!warp [optional: seconds] and !cancel",
            description = "Requests party warp with an optional warp delay.",
            category = "Commands",
            subcategory = "Party Commands"
    )
    public boolean warpCommand = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!coords",
            description = "Sends current location of user.",
            category = "Commands",
            subcategory = "Party Commands"
    )
    public boolean coordsCommand = false;

    // Guild Commands

    @Property(
            type = PropertyType.SWITCH,
            name = "Guild Commands",
            description = "Allows everyone in guild to use guild commands.\n/noba guildcommands",
            category = "Commands",
            subcategory = "Guild Commands"
    )
    public boolean guildCommands = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!help",
            description = "Sends all usable commands.",
            category = "Commands",
            subcategory = "Guild Commands"
    )
    public boolean guildHelpCommand = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "!warpout [username]",
            description = "Warp out a player.",
            category = "Commands",
            subcategory = "Guild Commands"
    )
    public boolean guildWarpOutCommand = false;

    // Notifiers

    @Property(
            type = PropertyType.SWITCH,
            name = "Cakes Eaten Notifier",
            description = "Notifies you once you've eaten all Century Cakes.",
            category = "Notifiers"
    )
    public boolean cakesEatenNotifier = false;

    @Property(
            type = PropertyType.NUMBER,
            name = "Current Number of Century Cakes",
            description = "Allows you to modify the number of century cakes when new ones are added.",
            category = "Notifiers",
            min = 1,
            max = 100
    )
    public int centuryCakesAmount = 14;

    @Property(
            type = PropertyType.SWITCH,
            name = "Totem of Corruption Notifier",
            description = "Notifies you once Totem of Corrupted expires.",
            category = "Notifiers"
    )
    public boolean corruptionTotemNotifier = false;

    // Dungeons

    @Property(
            type = PropertyType.SWITCH,
            name = "Camp Blood After Time",
            description = "A timer that tell you to camp blood after a certain amount of time.",
            category = "Dungeons",
            subcategory = "QOL"
    )
    public boolean bloodCampAfterTime = false;

    @Property(
            type = PropertyType.SLIDER,
            name = "Time (in seconds)",
            description = "Time until blood camp warning.",
            category = "Dungeons",
            subcategory = "QOL",
            min = 1,
            max = 30
    )
    public int timeTilBloodCamp = 30;

    @Property(
            type = PropertyType.SWITCH,
            name = "Simon Says Timer",
            description = "Announces how much time it took to complete the Simon Says device.",
            category = "Dungeons",
            subcategory = "QOL"
    )
    public boolean ssDeviceTimer = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "SS Time in Party Chat",
            description = "Instead of the message being client-side, send it in party chat.",
            category = "Dungeons",
            subcategory = "QOL"
    )
    public boolean ssDeviceTimerPC = false;

    // Dev

    @Property(
            type = PropertyType.SWITCH,
            name = "Debug Mode",
            description = "Only turn this on if you're testing something while editing code.",
            category = "Dev"
    )
    public boolean debugMode = false;

    public Config() {
        super(new File("./config/nobaaddons/" + NobaAddons.MOD_ID + ".toml"),  NobaAddons.MOD_NAME + " (" + NobaAddons.MOD_VERSION + ")");
        initialize();

        try {
            // DM Commands
            addDependency("warpMeCommand", "dmCommands");
            addDependency("partyMeCommand", "dmCommands");
            addDependency("dmWarpOutCommand", "dmCommands");
            // Party Commands
            addDependency("partyHelpCommand", "partyCommands");
            addDependency("transferCommand", "partyCommands");
            addDependency("allInviteCommand", "partyCommands");
            addDependency("warpCommand", "partyCommands");
            addDependency("coordsCommand", "partyCommands");
            // Guild Commands
            addDependency("guildHelpCommand", "guildCommands");
            addDependency("guildWarpOutCommand", "guildCommands");
            // Notifiers
            addDependency("timeTilBloodCamp", "bloodCampAfterTime");
            addDependency("centuryCakesAmount", "cakesEatenNotifier");
            // Dungeons
            addDependency("ssDeviceTimerPC", "ssDeviceTimer");
            markDirty();
        } catch(Exception e) {
            NobaAddons.LOGGER.error("Failed to add dependencies in config", e);
        }
    }
}
