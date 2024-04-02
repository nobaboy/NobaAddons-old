package me.nobaboy.nobaaddons.commands.subcommands;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.commands.ISubCommand;
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile;
import me.nobaboy.nobaaddons.util.ChatUtils;

import java.io.IOException;
import java.util.List;

public class SSRemoveLastCommand implements ISubCommand {
	@Override
	public String getName() {
		return "ssRemoveLast";
	}

	@Override
	public void run(String[] args) {
		List<Double> times = SSFile.INSTANCE.times.get();
		Double personalBest = SSFile.INSTANCE.personalBest.get();

		if(times.isEmpty()) {
			ChatUtils.addMessage("You have not completed a single Simon Says device in the Catacombs Floor 7.");
			return;
		}

		try {
			if(times.size() > 1) {
				ChatUtils.addMessage("Successfully removed last SS Time.");
				boolean isPb = times.get(times.size() - 1).equals(personalBest);
				times.remove(times.size() - 1);
				if(isPb) times.stream().mapToDouble(Double::doubleValue).min().ifPresent(SSFile.INSTANCE.personalBest::set);
			} else {
				ChatUtils.addMessage("Successfully cleared SS Times.");
				SSFile.INSTANCE.personalBest.set(null);
				times.clear();
			}
			SSFile.INSTANCE.save();
		} catch(IOException e) {
			NobaAddons.LOGGER.error("Failed to modify simon-says-times.json");
		}
	}
}
