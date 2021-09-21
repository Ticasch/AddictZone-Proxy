package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;

public class WartungManager {
    private String uuid;

    private final FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "Wartung.yml");

    public WartungManager(String uuid) throws IOException {
        this.uuid = uuid;
    }
    public void setWartung(boolean b) {
        this.fileBuilder.setValue("Status", b);
        this.fileBuilder.save();
    }
    public boolean getWartung() {
        return this.fileBuilder.getBoolean("Status");
    }
    public void setBypass(boolean b) {
        this.fileBuilder.setValue(this.uuid, b);
        this.fileBuilder.save();
    }
    public boolean getBypass() {
        return this.fileBuilder.getBoolean(this.uuid);
    }
}
