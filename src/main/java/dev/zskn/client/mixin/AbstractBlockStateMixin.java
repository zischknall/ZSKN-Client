package dev.zskn.client.mixin;

import dev.zskn.client.ZSKNClient;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow public abstract Block getBlock();

    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    void onGetLuminance(CallbackInfoReturnable<Integer> cir) {
        if (ZSKNClient.xrayToggle) {
            cir.setReturnValue(15);
        }
    }

    @Inject(method = "isSideInvisible", at = @At("HEAD"), cancellable = true)
    void onIsSideInvisible(BlockState state, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (ZSKNClient.xrayToggle) {
            if (!ZSKNClient.preciousList.contains(state.getBlock())) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "isOpaque", at = @At("HEAD"), cancellable = true)
    void onIsOpaque(CallbackInfoReturnable<Boolean> cir) {
        if (ZSKNClient.xrayToggle) {
            cir.setReturnValue(ZSKNClient.preciousList.contains(this.getBlock()));
        }
    }
}
