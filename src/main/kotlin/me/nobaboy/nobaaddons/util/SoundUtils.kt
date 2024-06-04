package me.nobaboy.nobaaddons.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import net.minecraft.client.audio.ISound
import net.minecraft.client.audio.PositionedSound
import net.minecraft.client.audio.SoundCategory
import net.minecraft.util.ResourceLocation

object SoundUtils {
    fun ISound.playSound() {
        mc.addScheduledTask {
            val gameSettings = mc.gameSettings
            val oldLevel = gameSettings.getSoundLevel(SoundCategory.PLAYERS)
            gameSettings.setSoundLevel(SoundCategory.PLAYERS, 1F)
            try {
                mc.soundHandler.playSound(this)
            } catch (ex: Exception) {
                if (ex is IllegalArgumentException) {
                    ex.message?.let {
                        if (it.startsWith("value already present:")) {
                            println("NobaAddons Sound Error: $it")
                            return@addScheduledTask
                        }
                    }
                }
                NobaAddons.LOGGER.error("Failed to play a sound, soundLocation ${this.soundLocation}", ex)
            } finally {
                gameSettings.setSoundLevel(SoundCategory.PLAYERS, oldLevel)
            }
        }
    }

    private fun createSound(name: String, pitch: Float, volume: Float = 50F): ISound {
        val sound: ISound = object : PositionedSound(ResourceLocation(name)) {
            init {
                this.pitch = pitch
                this.volume = volume
                repeat = false
                repeatDelay = 0
                attenuationType = ISound.AttenuationType.NONE
            }
        }
        return sound
    }

    fun playPlingSound(pitch: Float = 1F, volume: Float = 50F) = createSound("note.pling", pitch, volume).playSound()
    fun playBeepSound(pitch: Float = 1F, volume: Float = 50F) = createSound("random.orb", pitch, volume).playSound()
    fun playClickSound(pitch: Float = 1F, volume: Float = 50F) = createSound("gui.button.press", pitch, volume).playSound()
    fun playErrorSound(pitch: Float = 0F, volume: Float = 50F) = createSound("mob.endermen.portal", pitch, volume).playSound()

    fun repeatSound(delay: Long, repeat: Int, sound: String, pitch: Float = 1.0F, volume: Float = 1.0F) {
        NobaAddons.coroutineScope.launch {
            repeat(repeat) {
                createSound(sound, pitch, volume).playSound()
                delay(delay)
            }
        }
    }
}