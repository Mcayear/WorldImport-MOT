package cn.powernukkitx.worldimport;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.powernukkitx.worldimport.ws.client.WebSocketClient;

import java.io.IOException;

public class WorldImportCommand extends Command {
    public WorldImportCommand(String command) {
        super(command);
    }

    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!commandSender.isPlayer() || commandSender.isOp()) {
            if (strings.length == 0) {
                return false;
            }
            String subCmdName = strings[0];
            switch (subCmdName) {
                case "connect":
                    try {
                        WebSocketClient.start();
                        commandSender.sendMessage("§aConnect successfully!");
                        return true;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "disconnect": // ctc add name text enabletips
                    try {
                        WebSocketClient.stop();
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                case "trans":

                    break;
            }
        }
        return false;
    }
}
