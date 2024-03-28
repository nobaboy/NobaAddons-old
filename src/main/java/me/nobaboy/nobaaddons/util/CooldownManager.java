package me.nobaboy.nobaaddons.util;

public class CooldownManager {
	private long markedAt = 0;
	private long cooldownDuration = 3000L;

	public void startCooldown() {
		this.markedAt = System.currentTimeMillis();
	}

	public void startCooldown(long duration) {
		this.startCooldown();
		this.cooldownDuration = duration;
	}

	public boolean isOnCooldown() {
		long expiresAt = markedAt + cooldownDuration;
		return expiresAt > System.currentTimeMillis();
	}
}
