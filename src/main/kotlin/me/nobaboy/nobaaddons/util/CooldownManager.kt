package me.nobaboy.nobaaddons.util

open class CooldownManager {
    private var markedAt: Long = 0
    private var cooldownDuration: Long = 3000L

    fun startCooldown() {
        this.markedAt = System.currentTimeMillis()
    }

    fun startCooldown(duration: Long) {
        this.startCooldown()
        this.cooldownDuration = duration
    }

    fun isOnCooldown(): Boolean {
        val expiresAt = markedAt + cooldownDuration
        return expiresAt > System.currentTimeMillis()
    }
}