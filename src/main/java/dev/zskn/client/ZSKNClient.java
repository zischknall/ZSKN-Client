package dev.zskn.client;

import dev.zskn.client.features.Feature;
import dev.zskn.client.features.Features;
import dev.zskn.client.gui.ZSKNScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public class ZSKNClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ZSKN");
	private static final int textColor = (255 << 16) + (255 << 8) + (255) + (181 << 24);
	private static int screenHeight;
	private KeyBinding guiBind;

	public static List<Block> preciousList = List.of(
			Blocks.DIAMOND_ORE,
			Blocks.DEEPSLATE_DIAMOND_ORE,
			Blocks.GOLD_ORE,
			Blocks.DEEPSLATE_GOLD_ORE,
			Blocks.NETHER_GOLD_ORE,
			Blocks.IRON_ORE,
			Blocks.DEEPSLATE_IRON_ORE,
			Blocks.COPPER_ORE,
			Blocks.DEEPSLATE_COPPER_ORE,
			Blocks.IRON_ORE,
			Blocks.DEEPSLATE_IRON_ORE,
			Blocks.COAL_ORE,
			Blocks.DEEPSLATE_COAL_ORE,
			Blocks.NETHER_QUARTZ_ORE,
			Blocks.ANCIENT_DEBRIS,
			Blocks.EMERALD_ORE,
			Blocks.DEEPSLATE_EMERALD_ORE,
			Blocks.LAPIS_ORE,
			Blocks.DEEPSLATE_LAPIS_ORE,
			Blocks.REDSTONE_ORE,
			Blocks.DEEPSLATE_REDSTONE_ORE,
			Blocks.GOLD_BLOCK,
			Blocks.DIAMOND_BLOCK,
			Blocks.COAL_BLOCK,
			Blocks.REDSTONE_BLOCK,
			Blocks.LAPIS_BLOCK,
			Blocks.IRON_BLOCK,
			Blocks.COPPER_BLOCK,
			Blocks.NETHERITE_BLOCK,
			Blocks.CHEST,
			Blocks.TRAPPED_CHEST,
			Blocks.ENDER_CHEST,
			Blocks.RAW_COPPER_BLOCK,
			Blocks.RAW_GOLD_BLOCK,
			Blocks.RAW_IRON_BLOCK,
			Blocks.SCULK_SHRIEKER,
			Blocks.SCULK_SENSOR,
			Blocks.SCULK_CATALYST,
			Blocks.BEDROCK,
			Blocks.END_PORTAL,
			Blocks.END_PORTAL_FRAME,
			Blocks.NETHER_PORTAL,
			Blocks.OBSIDIAN,
			Blocks.DRAGON_EGG,
			Blocks.SHULKER_BOX,
			Blocks.BARREL,
			Blocks.BEACON,
			Blocks.ENCHANTING_TABLE,
			Blocks.TNT,
			Blocks.SPAWNER,
			Blocks.TORCH
	);

	@Override
	public void onInitializeClient() {
		List<Feature> features = Features.getAll();
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

		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			int xOffset = 1;
			int linesRendered = 0;

			for (Feature feature : features.stream().filter(feature -> feature.toggle).sorted(Comparator.comparingInt(feature -> MinecraftClient.getInstance().textRenderer.getWidth(feature.displayText))).toList()) {
				Text text = Text.of(feature.displayText);
				int textHeight = MinecraftClient.getInstance().textRenderer.fontHeight;
				MinecraftClient.getInstance().inGameHud.getTextRenderer().draw(matrixStack, text, xOffset, screenHeight - (textHeight * (linesRendered + 1)), textColor);
				linesRendered++;
			}

		});

		LOGGER.info("ZSKN initialized!");
	}
}
