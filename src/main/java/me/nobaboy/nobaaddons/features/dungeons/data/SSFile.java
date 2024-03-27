package me.nobaboy.nobaaddons.features.dungeons.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.celestialfault.celestialconfig.AbstractConfig;
import me.celestialfault.celestialconfig.variables.ArrayVariable;
import me.celestialfault.celestialconfig.variables.DoubleVariable;
import net.minecraftforge.fml.common.Loader;

import java.nio.file.Path;

public class SSFile extends AbstractConfig {
    private static final Path PATH = Loader.instance().getConfigDir().toPath().resolve("nobaaddons/simon-says-times.json");

    private SSFile() {
        super(PATH);
    }

    public static final SSFile INSTANCE = new SSFile();

    public final DoubleVariable personalBest = new DoubleVariable("personal_best", null);
    public final ArrayVariable<Double> times = new ArrayVariable<>("times", JsonPrimitive::new, JsonElement::getAsDouble);
}
