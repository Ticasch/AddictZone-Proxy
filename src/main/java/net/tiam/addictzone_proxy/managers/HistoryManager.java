package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HistoryManager {

    private String uuid;
    private String name;

    private final FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "History.yml");

    public HistoryManager(String name, String uuid) throws IOException {
        this.name = name;
        this.uuid = uuid;
    }
    public void setHistory(String ip, String reason, String expiry, long expirylong, String banner, String taker, String type, boolean taken, String count) {
        this.fileBuilder.setValue(this.uuid + ".ActuallyCount.All", Integer.valueOf(count));
        this.fileBuilder.setValue(this.uuid + "." + count + ".type", type);
        this.fileBuilder.setValue(this.uuid + "." + count + ".taken", taken);
        this.fileBuilder.setValue(this.uuid + "." + count + ".takenBy", taker);
        this.fileBuilder.setValue(this.uuid + "." + count + ".Name", this.name);
        this.fileBuilder.setValue(this.uuid + "." + count + ".Ip", ip);
        this.fileBuilder.setValue(this.uuid + "." + count + ".reason", reason);
        this.fileBuilder.setValue(this.uuid + "." + count + ".expiry", expiry);
        this.fileBuilder.setValue(this.uuid + "." + count + ".ExpiryLong", expirylong);
        this.fileBuilder.setValue(this.uuid + "." + count + ".bandate", System.currentTimeMillis());
        this.fileBuilder.setValue(this.uuid + "." + count + ".banner", banner);
        this.fileBuilder.save();
    }
    public void setAcctuallyCountMute(int count) {
        this.fileBuilder.setValue(this.uuid + ".ActuallyCount.Mutes", count);
        this.fileBuilder.save();
    }
    public void setAcctuallyCountBan(int count) {
        this.fileBuilder.setValue(this.uuid + ".ActuallyCount.Bans", count);
        this.fileBuilder.save();
    }
    public int getActuallyCountMute() {
        return this.fileBuilder.getInt(this.uuid + ".ActuallyCount.Mutes");
    }
    public int getActuallyCountBan() {
        return this.fileBuilder.getInt(this.uuid + ".ActuallyCount.Bans");
    }
    public void setBannedStatus (boolean status, int count) {
        this.fileBuilder.setValue(this.uuid + "." + count + ".Banned", status);
        this.fileBuilder.save();
    }
    public void settaken (boolean taken, String taker,  int count) {
        this.fileBuilder.setValue(this.uuid + "." + count + ".taken", taken);
        this.fileBuilder.setValue(this.uuid + "." + count + ".takenBy", taker);
        this.fileBuilder.save();
    }
    public int getActuallyCountAll() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return 0;
        }
        return this.fileBuilder.getInt(this.uuid + ".ActuallyCount.All");
    }
    public boolean getTaken(int count) {
        return this.fileBuilder.getBoolean(this.uuid + "." + count + ".taken");
    }
    public String getType(int count) {
        return this.fileBuilder.getString(this.uuid + "." + count + ".type");
    }
    public long getBandate(int count) {
        return this.fileBuilder.getLong(this.uuid + "." + count + ".bandate");
    }
    public String getTaker(int count) {
        return this.fileBuilder.getString(this.uuid + "." + count + ".takenBy");
    }
    public boolean getBanned(int count) {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return false;
        }
        return this.fileBuilder.getBoolean(this.uuid + "." + count + ".Banned");
    }
    public String getName(int count) {
        return this.fileBuilder.getString(this.uuid +"." +  count + ".Name");
    }
    public String getIp(int count) {
        return this.fileBuilder.getString(this.uuid + "." + count + ".Ip");
    }
    public String getReason(int count) {
        return this.fileBuilder.getString(this.uuid + "." + count + ".reason");
    }
    public String getExpiry(int count) {
        return this.fileBuilder.getString(this.uuid + "." + count + ".expiry");
    }
    public long getExpiryLong(int count) {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return 0;
        }
        return this.fileBuilder.getLong(this.uuid + "." + count + ".ExpiryLong");
    }
    public String getBanner(int count) {
        return this.fileBuilder.getString(this.uuid + "." + count + ".banner");
    }
}
