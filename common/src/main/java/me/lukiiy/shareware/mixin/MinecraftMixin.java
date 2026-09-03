package me.lukiiy.shareware.mixin;

import me.lukiiy.shareware.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Final private ShaderManager shaderManager;

    @Inject(method = "setLevel", at = @At("TAIL"))
    public void shareware$setupShader(ClientLevel level, CallbackInfo ci) {
        Constants.shader = shaderManager.getPostChain(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "shareware"), LevelTargetBundle.MAIN_TARGETS);
    }
}
