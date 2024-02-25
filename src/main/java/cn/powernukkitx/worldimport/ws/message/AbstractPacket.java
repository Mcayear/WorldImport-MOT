package cn.powernukkitx.worldimport.ws.message;


import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AbstractPacket implements PacketBase {
    private static final Gson gson = new Gson();

    // Abstract method for processing package
    public abstract void processPackage();

    public String toJsonString(){
        return gson.toJson(this);
    }
}