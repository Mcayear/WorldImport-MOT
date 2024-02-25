package cn.powernukkitx.worldimport.ws.message;

import cn.nukkit.level.format.LevelProvider;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;

@Data
public class WorldInfoResPacket extends AbstractPacket {
    private int pid = PacketID.WORLD_INFO_RES_PACKET;
    private boolean state = true;
    public ArrayList<RegionFileInfo> regions = new ArrayList<>();

    public WorldInfoResPacket(LevelProvider provider) {
        super();
        if (provider == null) {
            setState(false);
            return;
        }
        File directory = new File(provider.getPath()+"/region/");
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isFile()) {
                        continue;
                    }
                    regions.add(new RegionFileInfo(file));
                }
            }
        } else {
            setState(false);
        }
    }

    public static class RegionFileInfo {
        long size;
        int regionX;
        int regionZ;
        public RegionFileInfo(File file) {
            size = file.length();
            String[] names = file.getName().split("\\.");
            regionX = Integer.parseInt(names[1]);
            regionZ = Integer.parseInt(names[2]);
        }
    }

    @Override
    public void processPackage() {
    }
}
