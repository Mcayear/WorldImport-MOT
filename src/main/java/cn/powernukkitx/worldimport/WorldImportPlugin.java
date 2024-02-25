package cn.powernukkitx.worldimport;

import cn.nukkit.Server;
import cn.nukkit.lang.LangCode;
import cn.nukkit.lang.PluginI18n;
import cn.nukkit.lang.PluginI18nManager;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.powernukkitx.worldimport.config.MainConfig;
import cn.powernukkitx.worldimport.ws.WebSocketServerEndpoint;
import jakarta.websocket.DeploymentException;
import lombok.Getter;

/**
 * author: Mcayear
 */
public class WorldImportPlugin extends PluginBase {
    @Getter
    public static WorldImportPlugin instance;
    @Getter
    public static PluginI18n I18N;

    public static org.glassfish.tyrus.server.Server server;


    @Override
    public void onLoad() {
        //save Plugin Instance
        instance = this;
        //register the plugin i18n
        I18N = PluginI18nManager.register(this);
        //register the command of plugin
        this.getServer().getCommandMap().register("WorldImport", new WorldImportCommand("wi"));

        this.getLogger().info(TextFormat.WHITE + "I've been loaded!");
    }

    @Override
    public void onEnable() {
        //Use the plugin's i18n output
        this.getLogger().info(I18N.tr(LangCode.zh_CN,"exampleplugin.helloworld", "世界"));

        //PluginTask
        this.getServer().getScheduler().scheduleDelayedRepeatingTask(new BroadcastPluginTask(this), 20, 20);

        //Save resources
        this.saveResource("config.yml");
        MainConfig.init(new Config(this.getDataFolder()+"/config.yml", Config.YAML));

        if (MainConfig.server) {
            server = new org.glassfish.tyrus.server.Server(MainConfig.ip, MainConfig.port, "/worldexport", null, WebSocketServerEndpoint.class);

            try {
                server.start();
                this.getLogger().info("WebSocket Server started on port " + MainConfig.port + ".");
                // Keep the server running
                //Thread.currentThread().join();
            } catch (DeploymentException e) {
                this.getLogger().error("Failed to start WebSocket Server: " + e.getMessage());
            }
        } else {
            getLogger().info("使用 `/wi connect` 连接到服务器");
        }
    }

    @Override
    public void onDisable() {
        if (server != null) {
            server.stop();
        }
        this.getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
    }
}
