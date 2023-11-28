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
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ZSKNClient implements ClientModInitializer {
	public static final Logger ZSKNLogger = LoggerFactory.getLogger("ZSKN");
	public static final FriendsManager FRIENDS = new FriendsManager();
	private static final int textColor = (255 << 16) + (255 << 8) + (255) + (181 << 24);
	private static float screenHeight;
	private KeyBinding guiBind;
	private Boolean shouldShowFeatureToggles = false;
	private KeyBinding featureOverviewToggleBind;
	private KeyBinding oreRevealerBind;
	private List<Feature> features;

	@Override
	public void onInitializeClient() {
		Features.registerKeybindings();
		Features.loadFeatures();
		features = Features.getAll();
		FRIENDS.loadFriends();

		this.guiBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.zskn.gui",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_U,
				"category.zskn"
		));

		this.oreRevealerBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.zskn.oreRevealer",
				InputUtil.Type.KEYSYM,
				InputUtil.UNKNOWN_KEY.getCode(),
				"category.zskn"
		));

		this.featureOverviewToggleBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.zskn.features",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_PERIOD,
				"category.zskn"
		));

		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			for (Feature feature : features) {
				feature.onClientTick(client);
			}

			if (guiBind.wasPressed()) {
				client.setScreen(new ZSKNScreen());
			}
			if (oreRevealerBind.wasPressed()) {
				revealOres();
			}
			if (client.currentScreen != null) {
				screenHeight = client.currentScreen.height;
			}
			if (featureOverviewToggleBind.wasPressed()) {
				shouldShowFeatureToggles = !shouldShowFeatureToggles;
			}
		});

		ClientTickEvents.END_WORLD_TICK.register(world -> {
			for (Feature feature : features) {
				feature.onWorldTick(world);
			}
		});

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> Features.saveFeatures());

		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			if (!shouldShowFeatureToggles) {
				return;
			}
			float xOffset = 3f;
			int linesRendered = 0;
			ArrayList<Feature> toBeRendered = new ArrayList<>(features.stream().filter(feature -> feature.toggle).sorted(Comparator.comparingInt(feature -> MinecraftClient.getInstance().textRenderer.getWidth(feature.displayText))).toList());
			Collections.reverse(toBeRendered);

			for (Feature feature : toBeRendered) {
				OrderedText text = Text.of(feature.displayText).asOrderedText();
				int textHeight = MinecraftClient.getInstance().textRenderer.fontHeight;
				MinecraftClient.getInstance().inGameHud.getTextRenderer().drawWithOutline(text, xOffset, screenHeight - (textHeight * (linesRendered + 3)), textColor, 0, matrixStack.getMatrixStack().peek().getPositionMatrix(), matrixStack.getVertexConsumers(), 255);
				linesRendered++;
			}

		});

		LOGGER.info("ZSKN initialized!");
	}
}
