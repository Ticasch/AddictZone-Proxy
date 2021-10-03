package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;
import java.util.ArrayList;

public class UserManager {
    private String uuid;
    private String name;

    private FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "UserDataBase.yml");

    public UserManager(String name, String uuid) throws IOException {
        this.uuid = uuid;
        this.name = name;
    }
    public void setName() {
        this.fileBuilder.setValue(this.uuid + ".Name", this.name);
        this.fileBuilder.save();
    }
    public String getName() {
        return this.fileBuilder.getString(this.uuid + ".Name");
    }
    public void setJoins(int count) {
        this.fileBuilder.setValue(this.uuid + ".Join.Count", count);
        this.fileBuilder.save();
    }
    public int getJoins() {
        return this.fileBuilder.getInt(this.uuid + ".Join.Count");
    }
    public void setFirstJoin(long count) {
        this.fileBuilder.setValue(this.uuid + ".Join.First", count);
        this.fileBuilder.save();
    }
    public long getFirstJoin() {
        return this.fileBuilder.getLong(this.uuid + ".Join.First");
    }
    public void setLastJoin(long count) {
        this.fileBuilder.setValue(this.uuid + ".Join.Last", count);
        this.fileBuilder.save();
    }
    public long getLastJoin() {
        return this.fileBuilder.getLong(this.uuid + ".Join.Last");
    }
    public ArrayList<String> getUsers() {
        ArrayList<String> list = new ArrayList<>(this.fileBuilder.getKeys());
        return list;
    }
}
