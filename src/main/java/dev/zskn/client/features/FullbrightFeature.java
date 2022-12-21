package dev.zskn.client.features;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class FullbrightFeature extends Feature {

    public FullbrightFeature() {
        super("Fullbright", GLFW.GLFW_KEY_H);
    }

    @Override
    public void onClientTick(MinecraftClient client) {
        if (keybind.wasPressed()) {
            toggle = !toggle;
            client.worldRenderer.reload();
        }
    }
}
