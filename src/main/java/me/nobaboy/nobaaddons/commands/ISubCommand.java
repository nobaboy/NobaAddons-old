package me.nobaboy.nobaaddons.commands;

import java.util.Collections;
import java.util.List;

public interface ISubCommand {
	String getName();

	default List<String> getAliases() {
		return Collections.emptyList();
	}

	default boolean isEnabled() {
		return true;
	}

	void run(String[] args);
}
