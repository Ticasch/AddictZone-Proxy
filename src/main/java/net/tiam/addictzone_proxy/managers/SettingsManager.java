package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;

public class SettingsManager {

    private final FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "Settings.yml");

    public SettingsManager() throws IOException {
    }
    public void setSlots (int slots) {
        this.fileBuilder.setValue("Slots", slots);
        this.fileBuilder.save();
    }
    public int getSlots () {
        if (this.fileBuilder.getInt("Slots") == null) {
            return 0;
        }
        return this.fileBuilder.getInt("Slots");
    }
    public void setRankedSlots(int slots) {
        this.fileBuilder.setValue("Ranked-Slots", slots);
        this.fileBuilder.save();
    }
    public int getRankedSlots() {
        if (this.fileBuilder.getInt("Ranked_Slots") == null) {
            return 0;
        }
        return this.fileBuilder.getInt("Ranked-Slots");
    }
    public void setWartung(String uuid, boolean b) {
        this.fileBuilder.setValue("Wartung.Status", b);
        this.fileBuilder.save();
    }
    public boolean getWartung() {
        return this.fileBuilder.getBoolean("Wartung.Status");
    }
    public void setBypass(String uuid, boolean b) {
        this.fileBuilder.setValue(uuid, b);
        this.fileBuilder.save();
    }
    public boolean getBypass(String uuid) {
        return this.fileBuilder.getBoolean("Wartung.ByPass." + uuid);
    }
    public void setMOTD(String motd) {
        this.fileBuilder.setValue("motd", motd);
        this.fileBuilder.save();
    }
    public String getMOTD() {
        if (this.fileBuilder.getString("motd") == null) {
            return null;
        }
        return this.fileBuilder.getString("motd");
    }
}
