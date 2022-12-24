package dev.zskn.client.mixin;

import dev.zskn.client.features.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(FluidState.class)
public class FluidStateMixin {
    @Inject(method = "isEmpty", at = @At("HEAD"), cancellable = true)
    void onIsEmpty(CallbackInfoReturnable<Boolean> cir) {
        if (Features.BaseXray.toggle) {
            cir.setReturnValue(true);
        }
    }
}
