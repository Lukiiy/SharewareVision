package me.lukiiy.shareware;

import me.lukiiy.shareware.platform.Services;

public class CommonClass {
    public static void init() {
        Constants.LOG.info("Hello from Common init on {}!", Services.PLATFORM.getPlatformName());

        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) Constants.LOG.info("Hello to mod fire emoji");
    }
}