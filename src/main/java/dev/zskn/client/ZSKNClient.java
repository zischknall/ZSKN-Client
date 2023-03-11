package dev.zskn.client;

import dev.zskn.client.features.Feature;
import dev.zskn.client.features.Features;
import dev.zskn.client.gui.ZSKNScreen;
import dev.zskn.client.utils.FriendsManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ZSKNClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ZSKN");
	public static final FriendsManager FRIENDS = new FriendsManager();
	private static final int textColor = (255 << 16) + (255 << 8) + (255) + (181 << 24);
	private static int screenHeight;
	private KeyBinding guiBind;
	private List<Feature> features;

	@Override
	public void onInitializeClient() {
		Features.loadFeatures();
		features = Features.getAll();
		FRIENDS.loadFriends();

		this.guiBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.zskn.gui",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_U,
				"category.zskn"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			for (Feature feature : features) {
				feature.onClientTick(client);
			}

			if (guiBind.wasPressed()) {
				client.setScreen(new ZSKNScreen());
			}
			if (client.currentScreen != null) {
				screenHeight = client.currentScreen.height;
			}
		});

		ClientTickEvents.END_WORLD_TICK.register(world -> {
			for (Feature feature : features) {
				feature.onWorldTick(world);
			}
		});

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> Features.saveFeatures());

		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			int xOffset = 1;
			int linesRendered = 0;
			ArrayList<Feature> toBeRendered = new ArrayList<>(features.stream().filter(feature -> feature.toggle).sorted(Comparator.comparingInt(feature -> MinecraftClient.getInstance().textRenderer.getWidth(feature.displayText))).toList());
			Collections.reverse(toBeRendered);

			for (Feature feature : toBeRendered) {
				Text text = Text.of(feature.displayText);
				int textHeight = MinecraftClient.getInstance().textRenderer.fontHeight;
				MinecraftClient.getInstance().inGameHud.getTextRenderer().draw(matrixStack, text, xOffset, screenHeight - (textHeight * (linesRendered + 1)), textColor);
				linesRendered++;
			}

		});

		LOGGER.info("ZSKN initialized!");
	}
}
