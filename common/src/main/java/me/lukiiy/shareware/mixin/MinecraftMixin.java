package me.lukiiy.shareware.mixin;

import me.lukiiy.shareware.Constants;
import net.minecraft.client.GameLoadCookie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "onGameLoadFinished", at = @At("TAIL"))
    private void shareware$reload(GameLoadCookie cookie, CallbackInfo ci) {
        Minecraft.getInstance().getShaderManager().getPostChain(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "shareware"), LevelTargetBundle.MAIN_TARGETS);

        Constants.shaderFail = false;
    }
}
