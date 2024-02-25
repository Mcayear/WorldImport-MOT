package cn.powernukkitx.worldimport.ws.message;

public interface PacketBase {
    int pid = 0x00; // Package ID
    boolean state = false; // Package state
}
