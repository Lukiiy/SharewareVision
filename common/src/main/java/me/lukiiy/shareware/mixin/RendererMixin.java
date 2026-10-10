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
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = GameRenderer.class, priority = 10000)
public abstract class RendererMixin {
    @Unique
    private static final Identifier POST_ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "shareware");

    @Shadow public abstract RenderTarget mainRenderTarget();
    @Shadow @Final private CrossFrameResourcePool resourcePool;

    @Inject(method = "render", at = @At("TAIL"))
    private void shareware$apply(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        if (Constants.shader == null) return;

        Window window = Minecraft.getInstance().getWindow();

        try {
            PostChain chain = Minecraft.getInstance().getShaderManager().getPostChain(POST_ID, LevelTargetBundle.MAIN_TARGETS);
            if (chain == null) return;

            FrameGraphBuilder frameGraph = new FrameGraphBuilder();

            chain.addToFrame(frameGraph, window.getWidth(), window.getHeight(), PostChain.TargetBundle.of(PostChain.MAIN_TARGET_ID, frameGraph.importExternal("main", mainRenderTarget())));
            frameGraph.execute(resourcePool);
        } catch (Throwable e) {
            Constants.LOG.error("Whoops! ", e);
        }
    }
}