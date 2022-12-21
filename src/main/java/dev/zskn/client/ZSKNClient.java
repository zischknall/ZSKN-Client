package dev.zskn.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ZSKNClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ZSKN");

	private static KeyBinding xRayBinding;
	private static KeyBinding flyKeybinding;

	public static Boolean flyToggle = false;
	private final String flyText = "Fly";

	public static Boolean xrayToggle = false;
	private final String xrayText = "XRay";

	private int clientFloatingTicks = 0;
	private static final int textColor = (255 << 16) + (255 << 8) + (255) + (127 << 24);

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
		xRayBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.zskn.xray", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_X, // The keycode of the key
				"category.zskn" // The translation key of the keybinding's category.
		));

		flyKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.zskn.fly", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_V, // The keycode of the key
				"category.zskn" // The translation key of the keybinding's category.
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (xRayBinding.wasPressed()) {
				xrayToggle = !xrayToggle;
				client.worldRenderer.reload();
			}

			while (flyKeybinding.wasPressed()) {
				flyToggle = !flyToggle;

			}
		});

		ClientTickEvents.END_WORLD_TICK.register(world -> {
			MinecraftClient client = MinecraftClient.getInstance();
			ClientPlayerEntity player = client.player;
			if (player != null) {
				PlayerAbilities abilities = player.getAbilities();
				if (abilities.allowFlying != flyToggle) {
					abilities.allowFlying = flyToggle;
					if (!abilities.allowFlying) {
						abilities.flying = false;
					}
				}

				if (!abilities.flying && player.fallDistance > 2.0F) {
					Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY() + 0.1, player.getZ(), player.getYaw(), player.getPitch(), true));
				}

				if (flyToggle && !player.isOnGround()) {
					if (++clientFloatingTicks == 39) {
						Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY() - 0.05, player.getZ(), player.getYaw(), player.getPitch(), false));
					} else if (clientFloatingTicks == 41) {
						Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY() + 0.05, player.getZ(), player.getYaw(), player.getPitch(), false));
						clientFloatingTicks = 0;
					}
				} else {
					clientFloatingTicks = 0;
				}
			}
		});

		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			HashMap<String, Boolean> toggles = new HashMap<>(Map.of(flyText, flyToggle, xrayText, xrayToggle));
			int yOffset = 9;
			int xOffset = 1;
			int amount = 0;
			for (String text : toggles.keySet().stream().filter(toggles::get).toList()) {
				MinecraftClient.getInstance().inGameHud.getTextRenderer().draw(matrixStack, text, xOffset, (yOffset * amount) + 1, textColor);
				amount++;
			}
		});

		LOGGER.info("ZSKN initialized!");
	}

}
