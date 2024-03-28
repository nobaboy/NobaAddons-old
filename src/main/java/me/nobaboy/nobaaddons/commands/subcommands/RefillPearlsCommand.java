package me.nobaboy.nobaaddons.commands.subcommands;

import me.nobaboy.nobaaddons.NobaAddons;
import me.nobaboy.nobaaddons.commands.ISubCommand;
import me.nobaboy.nobaaddons.features.dungeons.PearlRefill;

public class RefillPearlsCommand implements ISubCommand {
	@Override
	public String getName() {
		return "refillpearls";
	}

	@Override
	public boolean isEnabled() {
		return NobaAddons.config.refillPearls;
	}

	@Override
	public void run(String[] args) {
		PearlRefill.refillPearls();
	}
}
