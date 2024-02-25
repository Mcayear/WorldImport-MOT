package cn.powernukkitx.worldimport.config;

import cn.nukkit.utils.Config;

public class MainConfig {
    public static boolean server;
    public static String ip;
    public static int port;
    public static int protocol;
    public static void init(Config cfg) {
        server = cfg.getBoolean("server", false);
        ip = cfg.getString("ip", "0.0.0.0");
        port = cfg.getInt("port", 5556);
        protocol = cfg.getInt("protocol", 649);
    }

}
