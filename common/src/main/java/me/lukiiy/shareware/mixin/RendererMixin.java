package me.lukiiy.shareware.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import me.lukiiy.shareware.Constants;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class RendererMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow public abstract RenderTarget mainRenderTarget();
    @Shadow @Final private CrossFrameResourcePool resourcePool;

    @Inject(method = "render", at = @At("TAIL"))
    private void shareware$apply(DeltaTracker deltaTracker, boolean bl, CallbackInfo ci) {
        if (Constants.shader != null) Constants.shader.process(mainRenderTarget(), resourcePool);
    }
}
