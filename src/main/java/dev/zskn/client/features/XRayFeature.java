package dev.zskn.client.features;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

import java.util.Set;

public class XRayFeature extends Feature {
    private static final Set<Block> allowList = Set.of(
            Blocks.DIAMOND_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.GOLD_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.NETHER_GOLD_ORE,
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

    public static boolean shouldRender(Block block) {
        return allowList.contains(block);
    }
}
