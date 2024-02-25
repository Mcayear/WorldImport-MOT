package cn.powernukkitx.worldimport.ws.message;

import lombok.Data;

import java.util.ArrayList;

@Data
public class RegionResPatchPacket extends AbstractPacket {
    private int pid = PacketID.REGION_RES_PATCH_PACKET;
    private boolean state = true;
    private ArrayList<RegionResPacket.BlockData> chunkData;

    public RegionResPatchPacket(ArrayList<RegionResPacket.BlockData> chunkData) {
        super();
        this.chunkData = chunkData;
    }


    @Override
    public void processPackage() {
    }
}
