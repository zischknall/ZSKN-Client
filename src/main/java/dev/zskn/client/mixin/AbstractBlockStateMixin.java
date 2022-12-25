package dev.zskn.client.mixin;

import dev.zskn.client.ZSKNClient;
import dev.zskn.client.features.BaseXRayFeature;
import dev.zskn.client.features.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow public abstract Block getBlock();

    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    void onGetLuminance(CallbackInfoReturnable<Integer> cir) {
        if (Features.Fullbright.toggle) {
            cir.setReturnValue(15);
        }
    }

    @Inject(method = "isAir", at = @At("HEAD"), cancellable = true)
    void onIsAir(CallbackInfoReturnable<Boolean> cir) {
        if (Features.XRay.toggle) {
            cir.setReturnValue(!ZSKNClient.preciousList.contains(this.getBlock()));
        }
        else if (Features.BaseXray.toggle) {
            cir.setReturnValue(!BaseXRayFeature.isBaseBlock(this.getBlock()));
        }
    }

    @Inject(method = "isSideInvisible", at = @At("HEAD"), cancellable = true)
    void onIsSideInvisible(BlockState state, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (Features.XRay.toggle) {
            if (ZSKNClient.preciousList.contains(state.getBlock())) {
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        } else if (Features.BaseXray.toggle) {
            if (BaseXRayFeature.isBaseBlock(state.getBlock())) {
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "isOpaque", at = @At("HEAD"), cancellable = true)
    void onIsOpaque(CallbackInfoReturnable<Boolean> cir) {
        if (Features.XRay.toggle) {
            cir.setReturnValue(false);
        } else if (Features.BaseXray.toggle) {
            cir.setReturnValue(false);
        }
    }
}
