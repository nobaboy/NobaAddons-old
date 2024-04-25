package me.nobaboy.nobaaddons.util.data;

public enum DungeonBoss {
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

    final String text;

    DungeonBoss(String text) {
        this.text = text;
    }

    public static DungeonBoss fromChat(String text) {
        for(DungeonBoss boss : DungeonBoss.values()) {
            if(text.contains(boss.text)) return boss;
        }
        return UNKNOWN;
    }
}
