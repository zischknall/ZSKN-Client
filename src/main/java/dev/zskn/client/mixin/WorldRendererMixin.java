package dev.zskn.client.mixin;

import dev.zskn.client.features.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "tickRainSplashing", at = @At("HEAD"), cancellable = true)
    void onTickRainSplashing(Camera camera, CallbackInfo ci) {
        if (Features.Fullbright.toggle || Features.XRay.toggle || Features.BaseXray.toggle) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    void onRenderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (Features.Fullbright.toggle || Features.XRay.toggle || Features.BaseXray.toggle) {
            ci.cancel();
        }
    }

    @Inject(method = "hasBlindnessOrDarkness", at = @At("HEAD"), cancellable = true)
    void onHasBlindnessOrDarkness(Camera camera, CallbackInfoReturnable<Boolean> cir){
        if (Features.Fullbright.toggle || Features.XRay.toggle || Features.BaseXray.toggle) {
            cir.setReturnValue(false);
        }
    }
}
