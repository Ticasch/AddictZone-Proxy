package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;

public class SecurityManager {
    private String ip;

    private final FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "Security.yml");

    public SecurityManager(String ip) throws IOException {
        this.ip = ip;
    }
    public void setIpCount(int count) {
        this.fileBuilder.setValue(this.ip, count);
        this.fileBuilder.save();
    }
    public int getIpCount() {
        if (this.fileBuilder.getInt(this.ip) == null) {
            return 0;
        }
        return this.fileBuilder.getInt(this.ip);
    }
}
