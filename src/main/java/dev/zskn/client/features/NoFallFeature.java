package dev.zskn.client.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class NoFallFeature extends Feature {

    public NoFallFeature() {
        super("NoFall", GLFW.GLFW_KEY_N);
    }

    @Override
    public void onWorldTick(ClientWorld world) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player != null) {
            if (toggle && (player.fallDistance > 2.0F || (player.getAbilities().flying && player.getVelocity().y < 0))) {
                Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY() + 0.1, player.getZ(), player.getYaw(), player.getPitch(), false));
            }
        }
    }
}
