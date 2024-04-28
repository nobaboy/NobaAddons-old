package me.nobaboy.nobaaddons.features.dungeons.data

import me.celestialfault.celestialconfig.AbstractConfig
import me.celestialfault.celestialconfig.Property
import me.nobaboy.nobaaddons.NobaAddons

object SSFile : AbstractConfig(NobaAddons.modDir.toPath().resolve("simon-says-times.json")) {
    var personalBest by Property.double("personal_best")
    val times by Property.list<Double>("times")
}