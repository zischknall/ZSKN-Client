package dev.zskn.client.features;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;

public abstract class Feature {
    public KeyBinding keybind;
    public Boolean toggle = false;
    public String displayText;

    public Feature(String display) {
        this.displayText = display;
        this.keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.zskn." + display.toLowerCase(),
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.zskn"
        ));
    }

    public void onClientTick(MinecraftClient client) {
        if (keybind.wasPressed()) { toggle(); }
    }

    public void onWorldTick(ClientWorld world) {}

    public void toggle() {
        toggle = !toggle;
    }

    public void enable() {
        if (!toggle) {
            toggle();
        }
    }

    public void disable() {
        if (toggle) {
            toggle();
        }
    }
}
