package cn.powernukkitx.worldimport.ws.message;

import lombok.Data;

@Data
public class WorldInfoRequestPacket extends AbstractPacket {
    private int pid = PacketID.WORLD_INFO_REQUEST_PACKET;
    private boolean state = true;
    public String world_name;

    public WorldInfoRequestPacket(String worldName) {
        super();
        world_name = worldName;
    }

    @Override
    public void processPackage() {
    }
}
