package cn.powernukkitx.worldimport.ws.client;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.format.leveldb.LevelDBProvider;
import cn.nukkit.level.generator.Void;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.level.Level;
import cn.powernukkitx.worldimport.WorldImportPlugin;
import cn.powernukkitx.worldimport.config.MainConfig;
import cn.powernukkitx.worldimport.ws.message.PacketID;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.websocket.*;
import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

@ClientEndpoint
public class WebSocketClient {
    private static final PluginLogger logger = WorldImportPlugin.getInstance().getLogger();
    private final Gson gson = new Gson();
    public static final ArrayList<String> worlds = new ArrayList<>();
    public static final ArrayList<String> regions = new ArrayList<>();
    public static Session session;
    public static Level level;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        logger.info("Connected to server.");
        WebSocketClient.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info("Connection closed. Reason: " + closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        thr.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        JsonObject json = gson.fromJson(message, JsonObject.class);
        if (!json.get("state").getAsBoolean()) {
            logger.warning("Package state is false, possible error.");
            return;
        }
        int pid = json.get("pid").getAsInt();
        switch (pid) {
            case PacketID.WORLD_LIST_RES_PACKET: {
                JsonArray worldsArray = json.getAsJsonArray("worlds");
                worlds.clear();
                for (JsonElement world : worldsArray) {
                    worlds.add(world.getAsString());
                }
                logger.info("All worlds: " + String.join(", ", worlds));
                break;
            }
            case PacketID.WORLD_INFO_RES_PACKET: {
                JsonArray regionsArray = json.getAsJsonArray("regions");

                regions.clear();
                for (JsonElement region : regionsArray) {
                    JsonObject regionObj = region.getAsJsonObject();
                    int regionX = regionObj.get("regionX").getAsInt();
                    int regionZ = regionObj.get("regionZ").getAsInt();
                    String regionStr = regionX + ":" + regionZ;
                    regions.add(regionStr);
                }

                for (JsonElement region : regionsArray) {
                    JsonObject regionObj = region.getAsJsonObject();
                    int regionX = regionObj.get("regionX").getAsInt();
                    int regionZ = regionObj.get("regionZ").getAsInt();
                    try {
                        session.getBasicRemote().sendText(gson.toJson(new RegionRequest(0x05, true, regionX, regionZ)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case PacketID.REGION_RES_PACKET: {
                if (!regions.isEmpty()) {
                    regions.remove(regions.size()-1);
                }
                JsonArray chunkDataArray = json.getAsJsonArray("chunkData");
                for (JsonElement ele : chunkDataArray) {
                    JsonObject blockObj = ele.getAsJsonObject();
                    placeBlock(blockObj);

                }
                break;
            }
            case PacketID.REGION_RES_PATCH_PACKET: {
                JsonArray chunkDataArray = json.getAsJsonArray("chunkData");
                for (JsonElement ele : chunkDataArray) {
                    JsonObject blockObj = ele.getAsJsonObject();
                    placeBlock(blockObj);
                }
                break;
            }
        }
    }

    public static void placeBlock(JsonObject blockObj) {
        if (level == null) {
            logger.error("level 对象为空，这种情况不应当存在");
            return;
        }
        Vector3 vec3 = new Vector3(blockObj.get("x").getAsInt(), blockObj.get("y").getAsInt(), blockObj.get("z").getAsInt());
        //Block block = Item.fromString(blockObj.get("name").getAsString()).getBlock();
        int fullId = GlobalBlockPalette.getLegacyFullId(MainConfig.protocol, blockObj.get("runtimeid").getAsInt());
        if (fullId == -1) {
            logger.warning("错误的方块 "+blockObj);
            return;
        }
        Block block = Block.get(fullId >> Block.DATA_BITS,fullId & Block.DATA_MASK);
        level.setBlock(vec3, 0, block, false, false);

    }

    public static void stop() throws IOException {
        if (session == null) return;
        session.close();
    }
    public static void start() throws Exception {
        WebSocketClient client = new WebSocketClient();
        ClientManager clientManager = ClientManager.createClient();
        clientManager.connectToServer(client, new URI("ws://0.0.0.0:"+ MainConfig.port +"/worldexport/"));
    }

    public static boolean genVoidWorld(String worldName) {
        if (Server.getInstance().loadLevel(worldName)) {
            logger.error("世界 "+worldName+" 已存在");
            return false;
        }
        Server.getInstance().generateLevel(worldName, 0, Void.class, new HashMap<>(), LevelDBProvider.class);
        Server.getInstance().loadLevel(worldName);
        return true;
    }

    public static class RegionRequest {
        int pid;
        boolean state;
        int region_x;
        int region_z;

        public RegionRequest(int pid, boolean state, int region_x, int region_z) {
            this.pid = pid;
            this.state = state;
            this.region_x = region_x;
            this.region_z = region_z;
        }
    }

    public static class WorldRequest {
        int pid;
        boolean state;
        String world_name;

        public WorldRequest(int pid, boolean state, String world_name) {
            this.pid = pid;
            this.state = state;
            this.world_name = world_name;
        }
    }
}
