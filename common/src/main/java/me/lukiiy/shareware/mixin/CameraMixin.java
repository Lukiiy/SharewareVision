package me.lukiiy.shareware.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.lukiiy.shareware.Common;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public class CameraMixin {
    @WrapOperation(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerp(FFF)F"))
    private float shareware$noLerp(float alpha1, float p0, float p1, Operation<Float> original) {
        if (Common.choppyCam.get()) return p0;

        return original.call(alpha1, p0, p1);
    }

    @WrapOperation(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerp(DDD)D"))
    private double shareware$noLerp2(double alpha1, double p0, double p1, Operation<Double> original) {
        if (Common.choppyCam.get()) return p0;

        return original.call(alpha1, p0, p1);
    }
}
