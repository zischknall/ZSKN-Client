package dev.zskn.client.features;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class XRayFeature extends Feature {

    public XRayFeature() {
        super("XRay", GLFW.GLFW_KEY_X);
    }

    @Override
    public void onClientTick(MinecraftClient client) {
        if (keybind.wasPressed()) {
            toggle = !toggle;
            client.worldRenderer.reload();
        }
    }
}
