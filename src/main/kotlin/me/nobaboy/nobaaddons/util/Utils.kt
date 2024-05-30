package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons
import java.awt.Desktop
import java.io.IOException
import java.net.URI

object Utils {
    fun onHypixel() = ScoreboardUtils.isInScoreboard(listOf("www.hypixel.net", "alpha.hypixel.net"))

    // Probably move this into its own utils class once I add more related functions
    fun openBrowser(url: String) {
        try {
            Desktop.getDesktop().browse(URI.create(url))
        } catch (ex: IOException) {
            NobaAddons.LOGGER.error("Failed to open link: $url", ex)
        }
    }

    fun getPlayerName(): String = NobaAddons.mc.thePlayer.name
}