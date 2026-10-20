package me.lukiiy.shareware;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Common.MOD_ID, dist = Dist.CLIENT)
public class SharewareVision {
    public SharewareVision(IEventBus eventBus) {
        CommonClass.init();
    }
}