package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;

public class AutoBanManager {

    private FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "AutoBanIp.yml");

    public AutoBanManager () throws IOException {
    }
    public void setIPStatus (String ip, boolean status) {
        this.fileBuilder.setValue(ip, status);
        this.fileBuilder.save();
    }
    public boolean getIPStatus (String ip) {
        if (this.fileBuilder.getString(ip) == null) {
            return false;
        }
        return this.fileBuilder.getBoolean(ip);
    }
}
