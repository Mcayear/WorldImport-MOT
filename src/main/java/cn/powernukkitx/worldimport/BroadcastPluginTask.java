package cn.powernukkitx.worldimport;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.scheduler.PluginTask;
import cn.powernukkitx.worldimport.ws.client.WebSocketClient;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * author: MagicDroidX
 * ExamplePlugin Project
 */
public class BroadcastPluginTask extends PluginTask<WorldImportPlugin> {
    private static final PluginLogger logger = WorldImportPlugin.getInstance().getLogger();
    private final Gson gson = new Gson();
    private int lastProcessIndex = 0;// region 的进度

    public BroadcastPluginTask(WorldImportPlugin owner) {
        super(owner);
    }

    @Override
    public void onRun(int currentTick) {
        if (WebSocketClient.session == null) return;
        if (!WebSocketClient.session.isOpen()) return;

        if (WebSocketClient.worlds.isEmpty()) {
            return;
        }

        if (lastProcessIndex != WebSocketClient.regions.size()) {
            lastProcessIndex = WebSocketClient.regions.size();
            logger.info("Remaining: " + WebSocketClient.regions.size());
        }

        if (!WebSocketClient.regions.isEmpty()) {
            return;
        }

        String worldName = WebSocketClient.worlds.remove(WebSocketClient.worlds.size() - 1);
        logger.info("Extracting world: " + worldName);
        try {
            if (WebSocketClient.genVoidWorld(worldName)) {
                WebSocketClient.level = Server.getInstance().getLevelByName(worldName);
                WebSocketClient.session.getBasicRemote().sendText(gson.toJson(new WebSocketClient.WorldRequest(0x03, true, worldName)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (WebSocketClient.worlds.isEmpty()) {
            logger.info("所有世界均转换完成！");
        }
    }
}
