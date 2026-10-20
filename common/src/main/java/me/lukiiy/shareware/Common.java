package me.lukiiy.shareware;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common {
    public static final String MOD_ID = "shareware"; // TODO gradle
    public static final String MOD_NAME = "Shareware Vision"; // TODO gradle
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final Identifier POST_ID = Identifier.fromNamespaceAndPath(MOD_ID, "shareware");

    public static boolean shaderFail = true;
}