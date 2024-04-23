package me.nobaboy.nobaaddons.features.chatcommands;

import java.util.Collections;
import java.util.List;

public interface IChatCommand {
    String name();

    default String usage() {
        return name();
    }

    void run(ChatContext ctx);

    default boolean isEnabled() {
        return true;
    }

    default List<String> aliases() {
        return Collections.emptyList();
    }
}