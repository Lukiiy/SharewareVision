package me.lukiiy.shareware.mixin;

import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @ModifyVariable(method = "extractRenderState", at = @At("HEAD"), argsOnly = true, name = "partialTicks")
    private float shareware$noLerp(float partialTicks) {
        return 0;
    }
}
