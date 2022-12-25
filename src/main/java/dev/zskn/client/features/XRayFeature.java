package dev.zskn.client.features;

import net.minecraft.client.MinecraftClient;

public class XRayFeature extends Feature {

    public XRayFeature() {
        super("XRay");
    }

    @Override
    public void toggle() {
        if (Features.BaseXray.toggle) {
            return;
        }
        super.toggle();
        MinecraftClient.getInstance().worldRenderer.reload();
    }
}
