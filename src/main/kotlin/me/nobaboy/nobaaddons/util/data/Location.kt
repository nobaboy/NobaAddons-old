package me.nobaboy.nobaaddons.util.data

import me.nobaboy.nobaaddons.util.StringUtils.lowercaseEquals

enum class Location(location: String) {
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
    GLACITE_MINESHAFT("Mineshaft"),
    GOLD_MINE("Gold Mine"),
    HUB("Hub"),
    JERRY_WORKSHOP("Jerry's Workshop"),
    KUUDRA("Kuudra"),
    PARK("The Park"),
    PRIVATE_ISLAND("Private Island"),
    RIFT("The Rift"),
    SPIDERS_DEN("Spider's Den");

    companion object {
        fun fromTab(text: String): Location {
            for (location in entries) {
                if (location.name.lowercaseEquals(text)) return location
            }
            return NONE
        }
    }
}