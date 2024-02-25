package cn.powernukkitx.worldimport.ws.message;

public interface PacketID {
    byte HELLO_PACKET = 0x01;
    byte WORLD_LIST_RES_PACKET = 0x02;
    byte WORLD_INFO_REQUEST_PACKET = 0X03;
    byte WORLD_INFO_RES_PACKET = 0X04;
    byte REGION_REQUEST_PACKET = 0X05;
    byte REGION_RES_PACKET = 0X06;
    byte REGION_RES_PATCH_PACKET = 0X08;
}
