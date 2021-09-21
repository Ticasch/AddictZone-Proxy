package net.tiam.addictzone_proxy.managers;

import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;

public class AccountManager {
    private String uuid;
    private String ip;
    private int collectcount;
    private int samecount;

    private final FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "Security.yml");

    public AccountManager(String ip, String uuid) throws IOException {
        this.ip = ip;
        this.uuid = uuid;
    }
    public void setUUIDName(String name) {
        this.fileBuilder.setValue(this.uuid + ".name", name);
        this.fileBuilder.save();
    }
    public void setIpName(String uuid, String name) {
        this.fileBuilder.setValue(this.ip + ".used." + uuid + ".name" , name);
        this.fileBuilder.save();
    }
    public void setActuallyIp(String ip) {
        this.fileBuilder.setValue(this.uuid + ".ActuallyIp", ip);
        this.fileBuilder.save();
    }
    public void setActuallyUUID(String uuid) {
        this.fileBuilder.setValue(this.ip + ".ActuallyUUID." + uuid, true);
        this.fileBuilder.save();
    }
    public void setUsedUUIDs(String uuid) {
        this.fileBuilder.setValue(this.ip + ".used." + uuid, true);
        this.fileBuilder.save();
    }
    public void setUsedIps(String ip) {
        this.fileBuilder.setValue(this.uuid + ".usedIps" + ip, true);
        this.fileBuilder.save();
    }
    public void setLastIp(String ip) {
        this.fileBuilder.setValue(this.uuid, ip);
        this.fileBuilder.save();
    }
    public String getLastIp() {
        if (this.fileBuilder.getString(this.uuid) == null) {
            return null;
        }
        return this.fileBuilder.getString(this.uuid);
    }
    public void setCollectCount(int count) {
        this.fileBuilder.setValue(this.ip + ".CollectedCount", count);
        this.fileBuilder.save();
    }
    public int getCollectCount() {
        if (this.fileBuilder.getInt(this.ip + ".CollectedCount") == null) {
            return 0;
        }
        return this.fileBuilder.getInt(this.ip + ".CollectedCount");
    }
    public void setSameCount(int count) {
        this.fileBuilder.setValue(this.ip + ".SameCount", count);
        this.fileBuilder.save();
    }
    public int getSameCount() {
        if (this.fileBuilder.getInt(this.ip + ".SameCount") == null) {
            return 0;
        }
        return this.fileBuilder.getInt(this.ip + ".SameCount");
    }

}
