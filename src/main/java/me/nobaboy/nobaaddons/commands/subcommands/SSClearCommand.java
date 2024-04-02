package me.nobaboy.nobaaddons.commands.subcommands;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.commands.ISubCommand;
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile;
import me.nobaboy.nobaaddons.util.ChatUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SSClearCommand implements ISubCommand {
	@Override
	public String getName() {
		return "ssClearTimes";
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("ssClear");
	}

	@Override
	public void run(String[] args) {
		List<Double> times = SSFile.INSTANCE.times.get();

		if(times.isEmpty()) {
			ChatUtils.addMessage("You have not completed a single Simon Says device in the Catacombs Floor 7.");
			return;
		}

		try {
			ChatUtils.addMessage("Successfully cleared SS Times.");
			SSFile.INSTANCE.personalBest.set(null);
			times.clear();
			SSFile.INSTANCE.save();
		} catch(IOException e) {
			NobaAddons.LOGGER.error("Failed to modify simon-says-times.json");
		}
	}
}
