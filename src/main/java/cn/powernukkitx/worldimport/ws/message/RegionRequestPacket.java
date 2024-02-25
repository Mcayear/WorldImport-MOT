package cn.powernukkitx.worldimport.ws.message;

import lombok.Data;

@Data
public class RegionRequestPacket extends AbstractPacket {
    private int pid = PacketID.REGION_REQUEST_PACKET;
    private boolean state = true;
    private int region_x;
    private int region_z;

    public RegionRequestPacket(int regionX, int regionZ) {
        super();
        region_x = regionX;
        region_z = regionZ;
    }

    @Override
    public void processPackage() {
    }
}
