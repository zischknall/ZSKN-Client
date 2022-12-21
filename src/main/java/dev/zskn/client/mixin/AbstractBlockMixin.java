package dev.zskn.client.mixin;

import dev.zskn.client.ZSKNClient;
import dev.zskn.client.features.Features;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(method = "getAmbientOcclusionLightLevel", at = @At("HEAD"), cancellable = true)
    void onGetAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (Features.Fullbright.toggle || Features.XRay.toggle) {
            cir.setReturnValue(1.0f);
        }
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    void onGetRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (Features.XRay.toggle) {
            if (!ZSKNClient.preciousList.contains(state.getBlock())) {
                cir.setReturnValue(BlockRenderType.INVISIBLE);
            }
        }
    }
}
