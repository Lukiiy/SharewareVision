package me.lukiiy.shareware.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {
    String getPlatformName();

    boolean isModLoaded(String modId);
    boolean isDevelopmentEnvironment();

    Path configDir();
}