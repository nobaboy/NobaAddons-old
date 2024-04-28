package me.nobaboy.nobaaddons.util.data

enum class DungeonBoss(name: String) {
    WATCHER("The Watcher"),
    BONZO("Bonzo"),
    SCARF("Scarf"),
    PROFESSOR("Professor"),
    THORN("Thorn"),
    LIVID("Livid"),
    SADAN("Sadan"),
    MAXOR("Maxor"),
    STORM("Storm"),
    GOLDOR("Goldor"),
    NECRON("Necron"),
    WITHER_KING("Wither King"),
    UNKNOWN("");

    companion object {
        fun fromChat(text: String): DungeonBoss {
            for (boss in entries) {
                if (text.contains(boss.name)) return boss
            }
            return UNKNOWN
        }
    }
}