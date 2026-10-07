package me.lukiiy.shareware;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class SharewareVision {
    public SharewareVision(IEventBus eventBus) {
        CommonClass.init();
    }
}