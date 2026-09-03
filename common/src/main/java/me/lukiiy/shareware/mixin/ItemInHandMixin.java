package me.lukiiy.shareware.mixin;

import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandMixin {
    @ModifyVariable(method = "submitHandsWithItems", at = @At("HEAD"), argsOnly = true, name = "frameInterp")
    private float shareware$noLerp(float frameInterp) {
        return 0;
    }
}
