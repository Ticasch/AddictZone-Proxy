package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;

public class MuteManager {
    private String name;
    private String uuid;

    private FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "Muted.yml");

    public MuteManager (String name, String uuid) throws IOException {
        this.name = name;
        this.uuid = uuid;
    }
    public void setMuted(String ip, String reason, String expiry, String muter, boolean ipmuted, boolean muted,  boolean permanently) {
        this.fileBuilder.setValue(this.uuid + ".Muted", muted);
        this.fileBuilder.setValue(this.uuid + ".Name", this.name);
        this.fileBuilder.setValue(this.uuid + ".Ip", ip);
        this.fileBuilder.setValue(this.uuid + ".reason", reason);
        this.fileBuilder.setValue(this.uuid + ".expiry", expiry);
        this.fileBuilder.setValue(this.uuid + ".muter", muter);
        this.fileBuilder.setValue(this.uuid + ".permanently", permanently);
        this.fileBuilder.save();
    }
    public boolean getMuted() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return false;
        }
        return this.fileBuilder.getBoolean(this.uuid + ".Muted");
    }
    public String getName() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return null;
        }
        return this.fileBuilder.getString(this.uuid + ".Name");
    }
    public String getIp() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return null;
        }
        return this.fileBuilder.getString(this.uuid + ".Ip");
    }
    public String getReason() {
        if (this.fileBuilder.getString(this.uuid + ".reason") == null) {
            return null;
        }
        return this.fileBuilder.getString(this.uuid + ".reason");
    }
    public String getExpiry() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return null;
        }
        return this.fileBuilder.getString(this.uuid + ".expiry");
    }
    public boolean getPermanently() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return false;
        }
        return this.fileBuilder.getBoolean(this.uuid + ".permanently");
    }
    public String getBanner() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return null;
        }
        return this.fileBuilder.getString(this.uuid + ".muter");
    }
    public boolean getIpStatus() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return false;
        }
        return this.fileBuilder.getBoolean(this.uuid + ".Ip.Status." + this.getIp());
    }
}
