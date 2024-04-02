package me.nobaboy.nobaaddons.commands.subcommands;

import me.nobaboy.nobaaddons.commands.ISubCommand;
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile;
import me.nobaboy.nobaaddons.util.ChatUtils;

import java.util.Collections;
import java.util.List;

public class SSPersonalBestCommand implements ISubCommand {
	@Override
	public String getName() {
		return "ssPersonalBest";
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("ssPB");
	}

	@Override
	public void run(String[] args) {
        Double personalBest = SSFile.INSTANCE.personalBest.get();
        String message = (personalBest == null) ?
                "You have not completed a single Simon Says device in the Catacombs Floor 7." :
                "Your SS PB is: " + personalBest;
        ChatUtils.addMessage(message);
	}
}
