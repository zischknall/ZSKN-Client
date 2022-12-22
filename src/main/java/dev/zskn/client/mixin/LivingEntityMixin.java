package dev.zskn.client.mixin;

import dev.zskn.client.features.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
    void onGetJumpVelocity(CallbackInfoReturnable<Float> cir) {
        if (Features.HighJump.toggle && ((LivingEntity) (Object) this) instanceof ClientPlayerEntity) {
            cir.setReturnValue(cir.getReturnValue() * 2f);
        }
    }
}
