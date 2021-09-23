package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;

public class AutoBanManager {

    private FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "AutoBanIp.yml");

    public AutoBanManager () throws IOException {
    }
    public void setIPStatusBanned (String ip, boolean status) {
        this.fileBuilder.setValue(ip + ".banned", status);
        this.fileBuilder.save();
    }
    public boolean getIPStatusBanned (String ip) {
        if (this.fileBuilder.getString(ip + ".banned") == null) {
            return false;
        }
        return this.fileBuilder.getBoolean(ip + ".banned");
    }
    public void setIpStatusMuted (String ip, boolean status) {
        this.fileBuilder.setValue(ip + ".muted", status);
        this.fileBuilder.save();
    }
    public boolean getIPStatusMuted (String ip) {
        if (this.fileBuilder.getString(ip + ".muted") == null) {
            return false;
        }
        return this.fileBuilder.getBoolean(ip + ".muted");
    }
}
