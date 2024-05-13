package me.nobaboy.nobaaddons

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.nobaboy.nobaaddons.api.PartyAPI
import me.nobaboy.nobaaddons.commands.NobaCommand
import me.nobaboy.nobaaddons.commands.SWikiCommand
import me.nobaboy.nobaaddons.core.Config
import me.nobaboy.nobaaddons.features.chatcommands.impl.DMCommands
import me.nobaboy.nobaaddons.features.chatcommands.impl.GuildCommands
import me.nobaboy.nobaaddons.features.chatcommands.impl.PartyCommands
import me.nobaboy.nobaaddons.features.dungeons.SimonSaysTimer
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile
import me.nobaboy.nobaaddons.features.misc.DisableMouse
import me.nobaboy.nobaaddons.features.misc.PearlRefill
import me.nobaboy.nobaaddons.features.notifiers.dungeons.CampBlood
import me.nobaboy.nobaaddons.features.notifiers.misc.CenturyCakes
import me.nobaboy.nobaaddons.features.notifiers.misc.TotemOfCorruption
import me.nobaboy.nobaaddons.keybinds.CommandKeyBind
import me.nobaboy.nobaaddons.keybinds.NobaKeyBind
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.DungeonUtils
import me.nobaboy.nobaaddons.util.LocationUtils
import net.minecraft.client.Minecraft
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.IOException

@Mod(
    modid = NobaAddons.MOD_ID,
    name = NobaAddons.MOD_NAME,
    version = NobaAddons.MOD_VERSION,
    acceptableSaveVersions = "[1.8.9]",
    clientSideOnly = true
)
class NobaAddons {
    companion object {
        const val MOD_ID = "nobaaddons"
        const val MOD_NAME = "NobaAddons"
        const val MOD_VERSION = "0.10.3"
        const val MOD_PREFIX = "§9NobaAddons §1> §b"

        val LOGGER: Logger = LogManager.getLogger(NobaAddons)
        var ticks = 0


        @JvmStatic
        val mc: Minecraft by lazy {
            Minecraft.getMinecraft()
        }

        val config by lazy {
            Config
        }

        val modDir by lazy {
            File(File(mc.mcDataDir, "config"), "nobaaddons").also {
                it.mkdirs()
            }
        }

        private val supervisorJob = SupervisorJob()
        val coroutineScope = CoroutineScope(
            CoroutineName("NobaAddons") + supervisorJob
        )
    }

    private val keybinds: MutableList<NobaKeyBind> = mutableListOf(
        CommandKeyBind("Pets Menu", "pets"),
        CommandKeyBind("Wardrobe Menu", "wardrobe"),
        CommandKeyBind("Equipment", "equipment"),
        CommandKeyBind("Ender Chest", "enderchest"),
        CommandKeyBind("Storage Menu", "storage"),
        NobaKeyBind("Disable Mouse", DisableMouse::toggleMouse),
        NobaKeyBind("Refill Pearls") { PearlRefill.refillPearls(true) }
    )

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        config.initialize()

        try {
            SSFile.load()
        } catch (ex: IOException) {
            LOGGER.error("Failed to load simon-says-times.json", ex)
        }

        keybinds.forEach(ClientRegistry::registerKeyBinding)
        arrayOf(
            this,
            
            // APIs
            PartyAPI,

            // Features
            PartyCommands(),
            GuildCommands(),
            DMCommands(),
            SimonSaysTimer(),
            CampBlood(),
            CenturyCakes(),
            TotemOfCorruption(),
            PearlRefill,

            // Util
            ChatUtils
        ).forEach(MinecraftForge.EVENT_BUS::register)
    }

    @Mod.EventHandler
    fun loadComplete(event: FMLLoadCompleteEvent) {
        val cch = ClientCommandHandler.instance

        // Main Command
        cch.registerCommand(NobaCommand())

        // Sub Commands
        cch.registerCommand(SWikiCommand())
    }

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        keybinds.forEach(NobaKeyBind::maybePress)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return

        if (++ticks % 20 == 0) {
            if (mc.thePlayer != null) {
                LocationUtils.checkForSkyblock()
                LocationUtils.checkTabLocation()
                DungeonUtils.checkForDungeonFloor()
                DungeonUtils.checkForDungeonClass()
            }
            ticks = 0
        }
    }
}