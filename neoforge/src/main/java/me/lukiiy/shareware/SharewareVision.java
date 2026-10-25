package me.lukiiy.shareware;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Common.MOD_ID, dist = Dist.CLIENT)
public class SharewareVision {
    public SharewareVision(IEventBus eventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (_, before) -> new ConfigScreen(before));
    }
}