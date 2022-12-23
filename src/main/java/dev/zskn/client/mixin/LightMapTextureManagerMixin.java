package dev.zskn.client.mixin;

import dev.zskn.client.features.Features;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightmapTextureManager.class)
public class LightMapTextureManagerMixin {
    @ModifyVariable(method = "update", at = @At("STORE"), ordinal = 1)
    private float overwriteL(float l) {
        if (Features.Fullbright.toggle) {
            return 1.0f;
        }
        return l;
    }
}
