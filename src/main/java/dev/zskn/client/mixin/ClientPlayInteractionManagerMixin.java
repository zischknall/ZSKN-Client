package dev.zskn.client.mixin;

import dev.zskn.client.features.Features;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayInteractionManagerMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "attackEntity", at = @At("HEAD"))
    void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (Features.AutoCrit.toggle && !player.getAbilities().flying) {
            MinecraftClient client = this.client;
            Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY() + 0.0625D, player.getZ(), player.getYaw(), player.getPitch(), true));
            Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch(), false));
            Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY() + 1.1E-5D, player.getZ(), player.getYaw(), player.getPitch(), false));
            Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch(), false));
        }
    }
}
