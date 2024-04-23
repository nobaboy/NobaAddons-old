package me.nobaboy.nobaaddons.commands;

import com.google.common.collect.Lists;
import gg.essential.api.utils.GuiUtil;
import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.commands.subcommands.*;
import me.nobaboy.nobaaddons.util.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.*;

public class NobaCommand extends CommandBase {
    private static final List<ISubCommand> COMMANDS = new ArrayList<>();

    static {
        COMMANDS.add(new HelpCommand());
        COMMANDS.add(new RefillPearlsCommand());
        COMMANDS.add(new SSAverageCommand());
        COMMANDS.add(new SSClearCommand());
        COMMANDS.add(new SSPersonalBestCommand());
        COMMANDS.add(new SSRemoveLastCommand());
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "nobaaddons";
    }

    @Override
    public List<String> getCommandAliases() {
        return Lists.newArrayList("noba");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Use '/noba help' to view all commands";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1) {
            GuiUtil.open(NobaAddons.config.gui());
            return;
        }

        String commandName = args[0].toLowerCase();
        Optional<ISubCommand> command = COMMANDS.stream()
                .filter(x -> {
                    if(x.getName().equalsIgnoreCase(commandName)) return true;
                    return x.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(commandName));
                })
                .findFirst();

        if(command.isPresent() && command.get().isEnabled()) {
            command.get().run(Arrays.copyOfRange(args, 1, args.length));
        } else {
            ChatUtils.addMessage(true,  "This command doesn't exist, use '/noba help' to view all commands.");
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        switch (args.length) {
            case 1:
                List<String> commands = new ArrayList<>();
                COMMANDS.stream().filter(ISubCommand::isEnabled).forEach(command -> {
                    commands.add(command.getName());
                    commands.addAll(command.getAliases());
                });
                return getListOfStringsMatchingLastWord(args, commands);
            case 2:
                if(args[0].equalsIgnoreCase("help")) {
                    return getListOfStringsMatchingLastWord(args, "dmCommands", "partyCommands", "guildCommands");
                }
            default:
                return Collections.emptyList();
        }
    }
}
