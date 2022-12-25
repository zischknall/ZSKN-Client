package dev.zskn.client.mixin;

import dev.zskn.client.features.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    void onShouldRender(Entity entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (Features.BaseXray.toggle) {
            cir.setReturnValue(false);
        } else if (Features.XRay.toggle && !(entity instanceof HostileEntity)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getBlockLight", at = @At("HEAD"), cancellable = true)
    void onGetBlockLight(Entity entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (Features.Fullbright.toggle) {
            cir.setReturnValue(15);
        }
    }

    @Inject(method = "getSkyLight", at = @At("HEAD"), cancellable = true)
    void onGetSkyLight(Entity entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (Features.Fullbright.toggle) {
            cir.setReturnValue(15);
        }
    }
}
