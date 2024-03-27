package me.nobaboy.nobaaddons.features.misc;

import me.nobaboy.nobaaddons.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DisableMouse {
    public static boolean disableMouse = false;
    static float sens = 0.5F;

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if(disableMouse) {
            Minecraft.getMinecraft().gameSettings.mouseSensitivity = sens;
            Minecraft.getMinecraft().gameSettings.saveOptions();
        }
        disableMouse = false;
    }

    public static void onDisableMouse() {
        disableMouse = !disableMouse;
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        if(disableMouse) {
            sens = gs.mouseSensitivity;
            gs.mouseSensitivity = -1/3F;
        } else {
            gs.mouseSensitivity = sens;
            gs.saveOptions();
        }
        ChatUtils.addMessage(disableMouse ? "Mouse movement disabled" : "Mouse movement enabled");
    }
}
