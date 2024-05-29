package me.nobaboy.nobaaddons.config.features.notifiers

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class CenturyCakesConfig {
    @Expose
    @JvmField
    @ConfigOption(name = "Enabled", desc = "Notifies you once you've eaten all Century Cakes.")
    @ConfigEditorBoolean
    var enabled = false

    @Expose
    @JvmField
    @ConfigOption(name = "Cakes Count", desc = "The current total number of Century Cakes that exist")
    @ConfigEditorSlider(minValue = 1F, maxValue = 100F, minStep = 1F)
    var cakesAmount = 14
}