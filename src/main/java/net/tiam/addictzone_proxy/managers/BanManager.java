package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class BanManager {

    private String uuid;
    private String name;

    private final FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "Banned.yml");

    public BanManager(String name, String uuid) throws IOException {
        this.name = name;
        this.uuid = uuid;
    }
    public void setBanned(String ip, String reason, String expiry, long expirylong, String banner, boolean banned,  boolean permanently) {
        this.fileBuilder.setValue(this.uuid + ".Banned", banned);
        this.fileBuilder.setValue(this.uuid + ".Name", this.name);
        this.fileBuilder.setValue(this.uuid + ".Ip", ip);
        this.fileBuilder.setValue(this.uuid + ".reason", reason);
        this.fileBuilder.setValue(this.uuid + ".expiry", expiry);
        this.fileBuilder.setValue(this.uuid + ".ExpiryLong", expirylong);
        this.fileBuilder.setValue(this.uuid + ".bandate", System.currentTimeMillis());
        this.fileBuilder.setValue(this.uuid + ".banner", banner);
        this.fileBuilder.setValue(this.uuid + ".permanently", permanently);
        this.fileBuilder.save();
    }
    public void setBannedStatus (boolean status) {
        this.fileBuilder.setValue(this.uuid + ".Banned", status);
        this.fileBuilder.save();
    }
    public boolean getBanned() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return false;
        }
        return this.fileBuilder.getBoolean(this.uuid + ".Banned");
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
    public long getExpiryLong() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return 0;
        }
        return this.fileBuilder.getLong(this.uuid + ".ExpiryLong");
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
        return this.fileBuilder.getString(this.uuid + ".banner");
    }
    public ArrayList<String> getBannedUsers() {
        ArrayList<String> list = new ArrayList<>(this.fileBuilder.getKeys());
        return list;
    }
    public void deleteBan() {
        this.fileBuilder.setValue(this.uuid + ".Banned", null);
        this.fileBuilder.setValue(this.uuid + ".Name", null);
        this.fileBuilder.setValue(this.uuid + ".Ip", null);
        this.fileBuilder.setValue(this.uuid + ".reason", null);
        this.fileBuilder.setValue(this.uuid + ".expiry", null);
        this.fileBuilder.setValue(this.uuid + ".ExpiryLong", null);
        this.fileBuilder.setValue(this.uuid + ".bandate", null);
        this.fileBuilder.setValue(this.uuid + ".banner", null);
        this.fileBuilder.setValue(this.uuid + ".permanently", null);
        this.fileBuilder.setValue(this.uuid, null);
        this.fileBuilder.save();
    }
}
