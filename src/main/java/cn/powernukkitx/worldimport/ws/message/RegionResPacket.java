package cn.powernukkitx.worldimport.ws.message;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.LevelProvider;
import lombok.Data;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@Data
public class RegionResPacket extends AbstractPacket {
    private int pid = PacketID.REGION_RES_PACKET;
    private boolean state = true;
    private ArrayList<BlockData> chunkData = new ArrayList<>();
    private String error;

    public RegionResPacket(LevelProvider provider, int regionX, int regionZ) {
        super();
    }

    public static class BlockData {
        String name;
        int x;
        int y;
        int z;
        Map<String, String> property = new TreeMap();
        public BlockData() {
        }
    }

    @Override
    public void processPackage() {
    }
}
