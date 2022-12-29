package dev.zskn.client.mixin;

import dev.zskn.client.ZSKNClient;
import dev.zskn.client.features.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (Features.ESP.toggle && ((Entity) (Object) this instanceof HostileEntity || (Entity) (Object) this instanceof PlayerEntity) && ((Object) this != MinecraftClient.getInstance().player)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getTeamColorValue", at = @At("HEAD"), cancellable = true)
    void onGetTeamColor(CallbackInfoReturnable<Integer> cir) {
        if (Features.ESP.toggle && (Entity) (Object) this instanceof PlayerEntity entity) {
            if (ZSKNClient.FRIENDS.contains(entity.getUuid())) {
                cir.setReturnValue(0x00FF00);
            } else {
                cir.setReturnValue(0xFF0000);
            }
        }
    }
}
