package me.lukiiy.shareware.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin<E extends Avatar & ClientAvatarEntity> {
    @Unique
    private static final float CUTOFF = 50f;

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void shareware$localPlayer(E entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || state.id != player.getId()) return;

        state.bodyRot = 0f;
        state.yRot = Mth.clamp(Mth.wrapDegrees(state.bodyRot + state.yRot), -CUTOFF, CUTOFF);
        if (!state.isFallFlying && state.swimAmount <= 0f) state.xRot = -state.xRot;
    }
}