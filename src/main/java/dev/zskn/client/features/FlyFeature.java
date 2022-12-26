package dev.zskn.client.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Objects;

public class FlyFeature extends Feature {

    private static int clientFloatingTicks = 0;

    public FlyFeature() {
        super("Fly");
    }

    @Override
    public void onWorldTick(ClientWorld world) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player != null && !player.isCreative() && !player.isSpectator()) {
            PlayerAbilities abilities = player.getAbilities();
            if (abilities.allowFlying != toggle) {
                abilities.allowFlying = toggle;
                if (!abilities.allowFlying) {
                    abilities.flying = false;
                }
            }

            if (toggle && !player.isOnGround()) {
                if (++clientFloatingTicks >= 20) {
                    PlayerMoveC2SPacket packet;
                    if (player.getVelocity().y >= 0) {
                        packet = new PlayerMoveC2SPacket.Full(player.getX(), player.getY() - 0.03130D, player.getZ(), player.getYaw(), player.getPitch(), false);
                    } else {
                        packet = new PlayerMoveC2SPacket.Full(player.getX(), player.getY() + 0.03130D, player.getZ(), player.getYaw(), player.getPitch(), false);
                    }
                    Objects.requireNonNull(client.getNetworkHandler()).sendPacket(packet);
                    clientFloatingTicks = 0;
                }
            } else {
                clientFloatingTicks = 0;
            }
        }
    }
}
