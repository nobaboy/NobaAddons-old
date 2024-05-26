package me.nobaboy.nobaaddons.config.features.dungeons

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class BloodCampTimerConfig {
    @Expose
    @JvmField
    @ConfigOption(name = "Enabled", desc = "A timer that tell you to camp blood after a certain amount of time.")
    @ConfigEditorBoolean
    var enabled = false

    @Expose
    @JvmField
    @ConfigOption(name = "Time (in seconds)", desc = "Time until blood camp warning.")
    @ConfigEditorSlider(minValue = 1f, maxValue = 30f, minStep = 1f)
    var timeTillWarning = 20
}