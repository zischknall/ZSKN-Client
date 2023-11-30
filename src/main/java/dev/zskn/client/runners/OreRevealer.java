package dev.zskn.client.runners;

import dev.zskn.client.utils.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static dev.zskn.client.ZSKNClient.ZSKNLogger;

public class OreRevealer implements Runnable {
    long delay;
    int rad;
    BlockState scanState = Blocks.GLASS.getDefaultState();

    public OreRevealer(int rad, long delay) {
        this.rad = rad;
        this.delay = delay;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        ClientPlayNetworkHandler conn = MinecraftClient.getInstance().getNetworkHandler();
        if (conn == null) return;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;

        Set<Block> checkblocks = Config.PaperAntiXrayEngineMode1;
        ConcurrentHashMap<BlockPos, BlockState> originalState = new ConcurrentHashMap<>();
        BlockPos pos = player.getBlockPos();

        for (int cx = -rad; cx <= rad; cx++) {
            for (int cy = -rad; cy <= rad; cy++) {
                for (int cz = -rad; cz <= rad; cz++) {
                    BlockPos currblock = new BlockPos(pos.getX() + cx, pos.getY() + cy, pos.getZ() + cz);
                    BlockState state = world.getBlockState(currblock);
                    Block block = state.getBlock();

                    boolean good = Config.scanAll;

                    for (Block checkblock : checkblocks) {
                        if (block.equals(checkblock)) {
                            good = true;
                            originalState.put(currblock, state);
                            Block.replace(state, scanState, world, currblock, Block.NOTIFY_ALL);
                            break;
                        }
                    }

                    if (!good) {
                        continue;
                    }

                    PlayerActionC2SPacket packet = new PlayerActionC2SPacket(
                            PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK,
                            currblock,
                            Direction.UP
                    );
                    conn.sendPacket(packet);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        ZSKNLogger.error("caught error while sleeping thread", e);
                    }
                }
            }
        }

        originalState.forEach((curr, state) -> {
            if (world.getBlockState(curr) == scanState) {
                Block.replace(scanState, state, world, curr, Block.NOTIFY_ALL);
            }
        });
        player.sendMessage(Text.of("Finished ore scan!"));
    }
}

