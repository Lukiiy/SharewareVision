package me.lukiiy.shareware.mixin;

import me.lukiiy.shareware.Constants;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(ReloadableResourceManager.class)
public class ReloadableResMixin {
    @Shadow @Final private PackType type;

    @Unique private static final Path PATH = FabricLoader.getInstance().getModContainer(Constants.MOD_ID).flatMap(container -> container.findPath("fabric.mod.json")).map(Path::getParent).orElse(null);
    @Unique private static final PackResources PACK_RES = PATH != null ? new PathPackResources(new PackLocationInfo(Constants.MOD_ID, Component.literal(Constants.MOD_ID + " Resources"), PackSource.BUILT_IN, Optional.empty()), PATH) : null;

    @ModifyVariable(method = "createReload", at = @At("HEAD"), argsOnly = true, name = "resourcePacks")
    private List<PackResources> shareware$load(List<PackResources> resourcePacks) {
        if (FabricLoader.getInstance().isModLoaded("fabric-resource-loader-v1") || type != PackType.CLIENT_RESOURCES || PACK_RES == null) return resourcePacks;

        List<PackResources> result = new ArrayList<>(resourcePacks.size() + 1);

        result.addAll(resourcePacks);
        result.add(PACK_RES);

        return result;
    }
}