package dev.zskn.client.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.Set;

public class Config {
    public static boolean scanAll = false;
    public static int rad = 5;
    public static long delay = 10;
    public static final Set<Block> PaperAntiXrayEngineMode1 = Set.of(
            Blocks.STONE,
            Blocks.DEEPSLATE,
            Blocks.NETHERRACK,
            Blocks.END_STONE,
            Blocks.OAK_PLANKS
    );
}
