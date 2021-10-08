package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;
import java.util.ArrayList;

public class WartungManager {

    private FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "Wartung.yml");

    public WartungManager() throws IOException {
    }
    public void setName(String uuid, String name) {
        this.fileBuilder.setValue(uuid, name);
        this.fileBuilder.save();
    }
    public String getName(String uuid) {
        return this.fileBuilder.getString(uuid);
    }
    public ArrayList<String> getBypassedUsers() {
        ArrayList<String> list = new ArrayList<>(this.fileBuilder.getKeys());
        return list;
    }
}
