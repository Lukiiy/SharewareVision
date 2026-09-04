package me.lukiiy.shareware.mixin;

import me.lukiiy.shareware.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private static void shareware$a(CallbackInfo ci) {
        Constants.shader = Minecraft.getInstance().getShaderManager().getPostChain(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "shareware"), LevelTargetBundle.MAIN_TARGETS);
    }
}
