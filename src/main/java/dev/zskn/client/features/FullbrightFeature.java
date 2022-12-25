package dev.zskn.client.features;

import net.minecraft.client.MinecraftClient;

public class FullbrightFeature extends Feature {

    public FullbrightFeature() {
        super("Fullbright");
    }

    @Override
    public void toggle() {
        super.toggle();
        MinecraftClient.getInstance().worldRenderer.reload();
    }
}
