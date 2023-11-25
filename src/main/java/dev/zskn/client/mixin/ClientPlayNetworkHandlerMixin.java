package dev.zskn.client.mixin;

import dev.zskn.client.features.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ClientCommonNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    List<ClientCommandC2SPacket.Mode> suppressedModes = List.of(
            ClientCommandC2SPacket.Mode.START_SPRINTING,
            ClientCommandC2SPacket.Mode.STOP_SPRINTING
    );

    @Redirect(method = "sendPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/packet/Packet;)V"))
    private void send(ClientConnection connection, Packet<?> packet) {
        if (Features.NoHunger.toggle) {
            if (packet instanceof PlayerInputC2SPacket inputC2SPacket) {
                connection.send(new PlayerInputC2SPacket(inputC2SPacket.getSideways(), inputC2SPacket.getForward(), false, inputC2SPacket.isSneaking()));
            } else if (packet instanceof ClientCommandC2SPacket commandC2SPacket) {
                if (!suppressedModes.contains(commandC2SPacket.getMode())) {
                    connection.send(packet);
                }
            } else {
                connection.send(packet);
            }
        } else {
            connection.send(packet);
        }
    }
}
