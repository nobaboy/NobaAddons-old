package me.nobaboy.nobaaddons.features.chatcommands.impl.shared;

import me.nobaboy.nobaaddons.features.chatcommands.ChatCommandManager;
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext;
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand;
import me.nobaboy.nobaaddons.util.ChatUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HelpCommand implements IChatCommand {
    private final ChatCommandManager manager;
    private final String command;
    private final Supplier<Boolean> isEnabled;

    public HelpCommand(ChatCommandManager manager, String command, Supplier<Boolean> enabled) {
        this.manager = manager;
        this.command = command;
        this.isEnabled = enabled;
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public void run(ChatContext ctx) {
        List<String> commands = this.manager.getCommands().stream()
                .map(x -> "!" + x.usage())
                .collect(Collectors.toList());

        ChatUtils.delayedSend(command + " NobaAddons > " + StringUtils.join(commands, ", "));
    }

    @Override
    public boolean isEnabled() {
        return isEnabled.get();
    }
}
