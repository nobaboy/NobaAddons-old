package me.nobaboy.nobaaddons.util.data;

/**
 * Taken from Danker's Skyblock Mod under GPL 3.0 license
 * <a href="https://github.com/bowser0000/SkyblockMod/blob/master/COPYING">link</a>
 * @author bowser0000
 */
public enum Location {
    NONE(""),
    CATACOMBS("Catacombs"),
    CRIMSON_ISLE("Crimson Isle"),
    CRYSTAL_HOLLOWS("Crystal Hollows"),
    DEEP_CAVERNS("Deep Caverns"),
    DUNGEON_HUB("Dungeon Hub"),
    DWARVEN_MINES("Dwarven Mines"),
    END("The End"),
    FARMING_ISLANDS("The Farming Islands"),
    GARDEN("Garden"),
    GOLD_MINE("Gold Mine"),
    HUB("Hub"),
    KUUDRA("Kuudra"),
    JERRY_WORKSHOP("Jerry's Workshop"),
    PARK("The Park"),
    PRIVATE_ISLAND("Private Island"),
    RIFT("The Rift"),
    SPIDERS_DEN("Spider's Den");

    final String text;

    Location(String text) {
        this.text = text;
    }

    public static Location fromTab(String text) {
        for(Location location : Location.values()) {
            if(location.text.equalsIgnoreCase(text)) return location;
        }
        return NONE;
    }
}