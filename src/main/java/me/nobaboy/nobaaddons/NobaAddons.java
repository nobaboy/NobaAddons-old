package me.nobaboy.nobaaddons;

import me.nobaboy.nobaaddons.commands.NobaCommand;
import me.nobaboy.nobaaddons.commands.SWikiCommand;
import me.nobaboy.nobaaddons.core.Config;
import me.nobaboy.nobaaddons.features.chatcommands.DMCommands;
import me.nobaboy.nobaaddons.features.chatcommands.GuildCommands;
import me.nobaboy.nobaaddons.features.chatcommands.PartyCommands;
import me.nobaboy.nobaaddons.features.dungeons.BloodCampTimer;
import me.nobaboy.nobaaddons.features.dungeons.PearlRefill;
import me.nobaboy.nobaaddons.features.dungeons.SSDeviceTimer;
import me.nobaboy.nobaaddons.features.dungeons.data.SSFile;
import me.nobaboy.nobaaddons.features.misc.DisableMouse;
import me.nobaboy.nobaaddons.features.notifiers.QOLNotifiers;
import me.nobaboy.nobaaddons.keybinds.CommandKeyBind;
import me.nobaboy.nobaaddons.keybinds.NobaKeyBind;
import me.nobaboy.nobaaddons.util.PartyUtils;
import me.nobaboy.nobaaddons.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Mod(modid = NobaAddons.MOD_ID, name = NobaAddons.MOD_NAME, version = NobaAddons.MOD_VERSION, acceptedMinecraftVersions = "[1.8.9]", clientSideOnly = true)
public class NobaAddons {
    public static final String MOD_ID = "nobaaddons";
    public static final String MOD_NAME = "NobaAddons";
    public static final String MOD_VERSION = "0.10.1";

//    public static final NobaAddons INSTANCE = new NobaAddons();
    public static final Logger LOGGER = LogManager.getLogger(NobaAddons.class);
    public static int ticks = 0;

    public static Config config = new Config();

    private static final List<NobaKeyBind> KEYBINDS = Arrays.asList(
            new CommandKeyBind("Pets Menu", "pets"),
            new CommandKeyBind("Wardrobe", "wardrobe"),
            new CommandKeyBind("Equipment Menu", "equipment"),
            new CommandKeyBind("Enderchest", "enderchest"),
            new CommandKeyBind("Storage Menu", "storage"),
            new NobaKeyBind("Disable Mouse", DisableMouse::onDisableMouse),
            new NobaKeyBind("Refill Pearls", () -> PearlRefill.refillPearls(true))
    );

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        config.preload();
        try {
            SSFile.INSTANCE.load();
        } catch(IOException e) {
            NobaAddons.LOGGER.error("Failed to load simon-says-times.json", e);
        }
        KEYBINDS.forEach(ClientRegistry::registerKeyBinding);
        Arrays.asList(
                this,
                new PartyUtils(),
                new PartyCommands(),
                new GuildCommands(),
                new DMCommands(),
                new BloodCampTimer(),
                new SSDeviceTimer(),
                new QOLNotifiers(),
                new PearlRefill(),
                new Utils() // Possibly move out dungeon phase checker to not have it here
        ).forEach(MinecraftForge.EVENT_BUS::register);
    }

    @Mod.EventHandler
    public void loadComplete(final FMLLoadCompleteEvent event) {
        ClientCommandHandler chh = ClientCommandHandler.instance;
        chh.registerCommand(new NobaCommand());
        chh.registerCommand(new SWikiCommand());
    }

    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        KEYBINDS.forEach(NobaKeyBind::maybePress);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;

        ticks++;
        if(ticks % 20 == 0) {
            if(player != null) {
                Utils.checkForSkyblock();
                Utils.checkTabLocation();
                Utils.checkForDungeonFloor();
                Utils.checkForDungeonClass();
            }
            ticks = 0;
        }
    }

    public static String getUsername() {
        return Minecraft.getMinecraft().thePlayer.getName();
    }
}