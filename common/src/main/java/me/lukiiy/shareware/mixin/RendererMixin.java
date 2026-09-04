package me.lukiiy.shareware.mixin;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.resource.ResourceHandle;
import me.lukiiy.shareware.Constants;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class RendererMixin {
    @Shadow public abstract RenderTarget mainRenderTarget();
    @Shadow @Final private CrossFrameResourcePool resourcePool;

    @Inject(method = "render", at = @At("TAIL"))
    private void shareware$apply(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        if (Constants.shader == null) return;

        Window window = Minecraft.getInstance().getWindow();
        FrameGraphBuilder frameGraph = new FrameGraphBuilder();
        ResourceHandle<RenderTarget> mainHandle = frameGraph.importExternal("main", mainRenderTarget());
        PostChain.TargetBundle targetBundle = PostChain.TargetBundle.of(PostChain.MAIN_TARGET_ID, mainHandle);

        Constants.shader.addToFrame(frameGraph, window.getWidth(), window.getHeight(), targetBundle);
        frameGraph.execute(resourcePool);
    }
}
