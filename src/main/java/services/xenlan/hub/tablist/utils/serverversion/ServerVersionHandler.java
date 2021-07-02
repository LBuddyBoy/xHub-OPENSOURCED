package services.xenlan.hub.tablist.utils.serverversion;



import org.bukkit.Bukkit;
import services.xenlan.hub.tablist.utils.serverversion.impl.ServerVersion1_8_R3Impl;
import services.xenlan.hub.tablist.utils.serverversion.impl.ServerVersionUnknownImpl;

public class ServerVersionHandler {

    public static IServerVersion version;
    public static String serverVersionName;

    public ServerVersionHandler() {
        serverVersionName = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        switch (serverVersionName) {
            case "v1_8_R3":
                version = new ServerVersion1_8_R3Impl();
                break;
            default:
                version = new ServerVersionUnknownImpl();
                break;
        }
    }
}
