package me.nobaboy.nobaaddons.features.chatcommands;

import jline.internal.Nullable;
import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.util.CooldownManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

public abstract class ChatCommandManager extends CooldownManager {
    private final List<IChatCommand> commands = new ArrayList<>();
    private final Object $lock = new Object();

    protected void register(IChatCommand command) {
        this.commands.add(command);
    }

    public List<IChatCommand> getCommands() {
        return commands;
    }

    /**
     * Match the provided message with a regex pattern for a possible chat command
     *
     * @param message The message received without any formatting codes
     * @return An {@link Optional} containing a {@link Matcher} with the named groups {@code username}, {@code command}, and {@code argument}
     */
    protected abstract Optional<Matcher> matchMessage(String message);

    public Optional<ChatContext> getContext(String message) {
        return matchMessage(message).map(match -> {
            String user = match.group("username");
            String command = match.group("command");
            @Nullable String args = match.group("argument");
            return new ChatContext(user, command, args != null ? args.split(" ") : new String[]{}, message);
        });
    }

    @SuppressWarnings("CodeBlock2Expr")
    public void processMessage(String message) {
        synchronized ($lock) {
            if(isOnCooldown()) return;

            getContext(message).ifPresent(ctx -> {
                commands.stream()
                        .filter(IChatCommand::isEnabled)
                        .filter(x -> x.name().equalsIgnoreCase(ctx.command()) ||
                                x.aliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(ctx.command())))
                        .findFirst()
                        .ifPresent(cmd -> {
                            try {
                                cmd.run(ctx);
                                if(!NobaAddons.config.debugMode) {
                                    startCooldown();
                                }
                            } catch(Exception e) {
                                // TODO proper error feedback
                                e.printStackTrace();
                            }
                        });
            });
        }
    }
}
