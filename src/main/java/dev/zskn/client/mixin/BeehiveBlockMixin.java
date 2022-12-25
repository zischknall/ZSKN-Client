package dev.zskn.client.mixin;

import dev.zskn.client.features.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BeehiveBlock.class)
public class BeehiveBlockMixin {
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    void onGetRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (Features.BaseXray.toggle || Features.XRay.toggle) {
            cir.setReturnValue(BlockRenderType.INVISIBLE);
        }
    }
}
