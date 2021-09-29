package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;

public class IPManager {
    private String uuid;
    private String name;

    private FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "IpDataBase.yml");

    public IPManager (String uuid, String name) throws IOException {
        this.uuid = uuid;
        this.name = name;
    }
    public void setIP (String ip) {
        this.fileBuilder.setValue(this.uuid + ".IP", ip);
        this.fileBuilder.save();
    }
    public void setName () {
        this.fileBuilder.setValue(this.uuid + ".Name", this.name);
        this.fileBuilder.save();
    }
    public String getIP () {
        if (this.fileBuilder.getString(this.uuid + ".Ip") == null) {
            return "0.0.0.0";
        }
        return this.fileBuilder.getString(this.uuid + ".IP");
    }
    public void setFullIp(String ip) {
        this.fileBuilder.setValue(this.uuid + ".IP-info.full", ip);
        this.fileBuilder.save();
    }
    public void setHostIP(String ip) {
        this.fileBuilder.setValue(this.uuid + ".IP-info.Host", ip);
        this.fileBuilder.save();
    }
}
