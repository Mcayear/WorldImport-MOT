package cn.powernukkitx.worldimport.ws;

import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.anvil.Chunk;
import cn.nukkit.level.format.anvil.RegionLoader;
import cn.nukkit.math.BlockVector3;
import cn.powernukkitx.worldimport.WorldImportPlugin;
import cn.powernukkitx.worldimport.ws.message.RegionResPacket;
import cn.powernukkitx.worldimport.ws.message.RegionResPatchPacket;
import javax.websocket.Session;

import java.io.IOException;
import java.util.ArrayList;

public class HandleRegionRequest {
    private static ArrayList<RegionResPacket.BlockData> chunkData = new ArrayList<>();
    public static void start(Session session, LevelProvider provider, int regionX, int regionZ) throws IOException {
        RegionResPacket packet = new RegionResPacket(provider, regionX, regionZ);
        if (provider == null) {
            return;
        }
        RegionLoader loader;
        Chunk chunk;
        try {
            loader = new RegionLoader(provider, regionX, regionZ);
            for (int x = 0; x < 32; x++) {
                for (int z = 0; z < 32; z++) {
                    chunk = loader.readChunk(x, z);
                    if (chunk == null) {
                        continue;
                    }
                    //WorldExportPlugin.INSTANCE.getLogger().info("sector total count："+chunk.getChunkSectionCount());
                    ChunkSection[] sections = chunk.getSections();
                    for (ChunkSection section : sections) {
                        BlockVector3 current = new BlockVector3();
                        int offsetY = section.getY() << 4;
                        for (int xx = 0; xx < 16; xx++) {
                            current.x = (chunk.getX() << 4) + xx;
                            for (int zz = 0; zz < 16; zz++) {
                                current.z = (chunk.getZ() << 4) + zz;
                                for (int y = 0; y < 16; y++) {
                                    current.y = offsetY + y;
                                    //Block block = section.getBlockState(xx, y, zz).getBlockRepairing(provider.getLevel(), current, 0);
                                    //if (block.getId() == Block.AIR) continue;
                                    //chunkData.add(new RegionResPacket.BlockData(block));
                                }
                            }
                        }
                        session.getBasicRemote().sendText(new RegionResPatchPacket(chunkData).toJsonString());
                        chunkData.clear();
                    }
                }
            }
        } catch (IOException e) {
            packet.setError(e.getCause().toString());
            packet.setState(false);
            WorldImportPlugin.getInstance().getLogger().error(provider.getLevel().getName()+" 世界的 r."+regionX+"."+regionZ+".mca 文件有误："+e.getCause().toString());
        }
        session.getBasicRemote().sendText(packet.toJsonString());
    }
}
