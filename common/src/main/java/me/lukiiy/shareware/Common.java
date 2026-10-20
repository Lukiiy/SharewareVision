package me.lukiiy.shareware;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common {
    public static final String MOD_ID = "shareware"; // TODO gradle
    public static final String MOD_NAME = "Shareware Vision"; // TODO gradle
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final Identifier POST_ID = Identifier.fromNamespaceAndPath(MOD_ID, "shareware");
    public static final Config config = new Config("shareware", "Shareware Vision");
    public static final Config.Key<Boolean> choppyCam = config.key("choppyCam", true);
    public static final Config.Key<Boolean> limitedRotPlayer = config.key("staticPlayer", true);
    public static final Config.Key<Boolean> billboardMobs = config.key("billboardMobs", true);

    public static boolean shaderFail = true;
}