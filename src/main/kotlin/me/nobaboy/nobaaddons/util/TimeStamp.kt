package me.nobaboy.nobaaddons.util

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@JvmInline
value class TimeStamp(private val millis: Long): Comparable<TimeStamp> {
    operator fun unaryMinus() = TimeStamp(-millis)

    operator fun plus(duration: Duration) = TimeStamp(millis + duration.inWholeMilliseconds)
    operator fun plus(milliseconds: Long) = TimeStamp(millis + milliseconds)
    operator fun plus(other: TimeStamp) = (millis + other.millis).milliseconds

    operator fun minus(duration: Duration) = TimeStamp(millis - duration.inWholeMinutes)
    operator fun minus(milliseconds: Long) = TimeStamp(millis - milliseconds)
    operator fun minus(other: TimeStamp) = (millis - other.millis).milliseconds

    fun elapsedSince() = currentTime() - this

    fun timeRemaining() = -elapsedSince()

    fun isPast(): Boolean = timeRemaining().isNegative()
    fun isFuture(): Boolean = timeRemaining().isPositive()
    fun isDistantPast(): Boolean = millis == 0L

    override fun compareTo(other: TimeStamp): Int = millis.compareTo(other.millis)

    fun toMillis(): Long = millis

    companion object {
        fun currentTime() = TimeStamp(System.currentTimeMillis())
        fun distantPast() = TimeStamp(0)
        fun distantFuture() = TimeStamp(Long.MAX_VALUE)

        fun Duration.fromNow() = currentTime() + this

        fun Long.asTimeStamp() = TimeStamp(this)
    }
}