package dev.zskn.client.mixin;

import dev.zskn.client.ZSKNClient;
import dev.zskn.client.features.BaseXRayFeature;
import dev.zskn.client.features.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BlockWithEntity.class)
public class BlockWithEntityMixin {
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    void onGetRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (Features.XRay.toggle) {
            if (!ZSKNClient.preciousList.contains(state.getBlock())) {
                cir.setReturnValue(BlockRenderType.INVISIBLE);
            }
        } else if (Features.BaseXray.toggle) {
            if (!BaseXRayFeature.isBaseBlock(state.getBlock())) {
                cir.setReturnValue(BlockRenderType.INVISIBLE);
            }
        }
    }
}
