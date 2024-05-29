package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.util.StringUtils.cleanString
import net.minecraft.inventory.Container
import net.minecraft.inventory.ContainerChest
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

    val ContainerChest.name: String
        get() = this.lowerChestInventory.displayName.unformattedText.cleanString()

    val Container.name: String
        get() = (this as? ContainerChest)?.name ?: "Undefined Container"
}