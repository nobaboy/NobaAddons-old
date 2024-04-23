package me.nobaboy.nobaaddons.commands.subcommands;

import me.nobaboy.nobaaddons.commands.ISubCommand;
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile;
import me.nobaboy.nobaaddons.util.ChatUtils;

import java.util.Collections;
import java.util.List;

public class SSAverageCommand implements ISubCommand {
	@Override
	public String getName() {
		return "ssAverage";
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("ssAvg");
	}

	@Override
	public void run(String[] args) {
		double sum = 0;
		long count = 0;

		List<Double> times = SSFile.INSTANCE.times.get();
		for(Double time : times) {
			sum += time;
			count++;
		}
		if(count == 0) {
			ChatUtils.addMessage("You have not completed a single Simon Says device in the Catacombs Floor 7.");
			return;
		}
		double average = Math.round((sum / count) * 1000.0) / 1000.0;
		ChatUtils.addMessage("Your average time for Simon Says is: " + average + "s (Total SS Devices: " + count + ")");
	}
}
