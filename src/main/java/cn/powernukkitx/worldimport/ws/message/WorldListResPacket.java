package cn.powernukkitx.worldimport.ws.message;

import cn.nukkit.Server;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;

@Data
public class WorldListResPacket extends AbstractPacket {
    private int pid = PacketID.WORLD_LIST_RES_PACKET;
    private boolean state = true;
    public ArrayList<String> worlds = new ArrayList<>();

    public WorldListResPacket() {
        super();
        File directory = new File(Server.getInstance().getDataPath()+"worlds/");
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        worlds.add(file.getName());
                    }
                }
            }
        } else {
            setState(false);
        }
    }
    @Override
    public void processPackage() {

    }
}
