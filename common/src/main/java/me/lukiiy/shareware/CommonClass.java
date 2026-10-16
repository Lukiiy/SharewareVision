package me.lukiiy.shareware;

public class CommonClass {
    public static Config config = null;

    public static void init() {
        config = new Config("shareware", "Shareware Vision");
    }
}