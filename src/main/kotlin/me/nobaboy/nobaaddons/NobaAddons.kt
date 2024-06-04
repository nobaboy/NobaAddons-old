package me.nobaboy.nobaaddons

import io.github.notenoughupdates.moulconfig.managed.ManagedConfig
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.nobaboy.nobaaddons.api.PartyAPI
import me.nobaboy.nobaaddons.commands.NobaCommand
import me.nobaboy.nobaaddons.commands.SWikiCommand
import me.nobaboy.nobaaddons.config.NobaConfig
import me.nobaboy.nobaaddons.features.chat.HideTipMessages
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
import me.nobaboy.nobaaddons.util.PlayerUtils
import me.nobaboy.nobaaddons.util.SkyblockUtils
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
    acceptedMinecraftVersions = "[1.8.9]",
    clientSideOnly = true
)
class NobaAddons {
    companion object {
        const val MOD_ID = "nobaaddons"
        const val MOD_NAME = "NobaAddons"
        const val MOD_VERSION = "0.10.6"
        const val MOD_PREFIX = "§9NobaAddons §1> §b"

        val LOGGER: Logger = LogManager.getLogger(NobaAddons)
        var ticks = 0
        var openConfigGui = false

        @JvmStatic
        val mc: Minecraft by lazy {
            Minecraft.getMinecraft()
        }

        val configManager by lazy {
            ManagedConfig.create(NobaConfig.FILE, NobaConfig::class.java)
        }

        val config by lazy { configManager.instance }

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
        config

        try {
            SSFile.load()
            if (SSFile.personalBest !in SSFile.times) {
                SSFile.personalBest = SSFile.times.minOrNull().takeIf { it?.isNaN() == false }
                SSFile.save()
            }
        } catch (ex: IOException) {
            LOGGER.error("Failed to load simon-says-times.json", ex)
        }

        keybinds.forEach(ClientRegistry::registerKeyBinding)
        arrayOf(
            // Objects that don't fit into any category
            this,

            // APIs
            PartyAPI,

            // Chat
            HideTipMessages(),

            // Chat Commands
            PartyCommands(),
            GuildCommands(),
            DMCommands(),

            // Dungeons
            SimonSaysTimer(),
            CampBlood(),

            // Notifiers
            CenturyCakes(),
            TotemOfCorruption(),

            // Misc
            PearlRefill,

            // Util
            ChatUtils,
            DungeonUtils
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

        if(openConfigGui) {
            configManager.openConfigGui()
            openConfigGui = false
        }

        if (++ticks % 20 == 0) {
            if (mc.thePlayer != null) {
                SkyblockUtils.checkForSkyblock()
                SkyblockUtils.checkTabLocation()
                DungeonUtils.checkForDungeonFloor()
                DungeonUtils.checkForDungeonClass()
                PlayerUtils.processClickQueue()
            }
            ticks = 0
        }
    }
}