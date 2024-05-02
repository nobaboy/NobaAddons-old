package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons

object Utils {
    fun onHypixel() = ScoreboardUtils.isInScoreboard(listOf("www.hypixel.net", "alpha.hypixel.net"))

    fun getPlayerName(): String = NobaAddons.mc.thePlayer.name
}