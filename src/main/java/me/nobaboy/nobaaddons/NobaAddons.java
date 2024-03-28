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
import me.nobaboy.nobaaddons.util.ChatUtils;
import me.nobaboy.nobaaddons.util.PartyUtils;
import me.nobaboy.nobaaddons.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
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
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Mod(modid = NobaAddons.MOD_ID, name = NobaAddons.MOD_NAME, version = NobaAddons.MOD_VERSION, acceptedMinecraftVersions = "[1.8.9]", clientSideOnly = true)
public class NobaAddons {
    public static final String MOD_ID = "nobaaddons";
    public static final String MOD_NAME = "NobaAddons";
    public static final String MOD_VERSION = "0.9.9";
    public static final String PLAYER_IGN = Minecraft.getMinecraft().getSession().getUsername();

//    public static final NobaAddons INSTANCE = new NobaAddons();
    public static final Logger LOGGER = LogManager.getLogger(NobaAddons.class);
    public static int ticks = 0;

    public static Config config = new Config();

    public static List<KeyBinding> keyBinds = Arrays.asList(
            new KeyBinding("Pets Menu", Keyboard.KEY_V, MOD_NAME),
            new KeyBinding("Wardrobe", Keyboard.KEY_LMENU, MOD_NAME),
            new KeyBinding("Equipment Menu", Keyboard.KEY_H, MOD_NAME),
            new KeyBinding("Enderchest", Keyboard.KEY_NONE, MOD_NAME),
            new KeyBinding("Storage Menu", Keyboard.KEY_NONE, MOD_NAME),
            new KeyBinding("Disable Mouse", Keyboard.KEY_NONE, MOD_NAME),
            new KeyBinding("Refill Pearls", Keyboard.KEY_NONE, MOD_NAME)
    );

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        config.preload();
        try {
            SSFile.INSTANCE.load();
        } catch(IOException e) {
            NobaAddons.LOGGER.error("Failed to load simon-says-times.json", e);
        }
        keyBinds.forEach(ClientRegistry::registerKeyBinding);
        Arrays.asList(
                this,
                new PartyUtils(),
                new PartyCommands(),
                new GuildCommands(),
                new DMCommands(),
                new BloodCampTimer(),
                new SSDeviceTimer(),
                new QOLNotifiers(),
                new PearlRefill()
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
        if(keyBinds.get(0).isPressed()) ChatUtils.sendCommand("pets");
        if(keyBinds.get(1).isPressed()) ChatUtils.sendCommand("wardrobe");
        if(keyBinds.get(2).isPressed()) ChatUtils.sendCommand("equipment");
        if(keyBinds.get(3).isPressed()) ChatUtils.sendCommand("enderchest");
        if(keyBinds.get(4).isPressed()) ChatUtils.sendCommand("storage");
        if(keyBinds.get(5).isPressed()) DisableMouse.onDisableMouse();
        if(keyBinds.get(6).isPressed()) PearlRefill.refillPearls();
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
            }
            ticks = 0;
        }
    }
}