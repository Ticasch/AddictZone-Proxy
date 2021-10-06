package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
    public void setWartung(boolean b) {
        this.fileBuilder.setValue("Wartung", b);
        this.fileBuilder.save();
    }
    public boolean getWartung() {
        return this.fileBuilder.getBoolean("Wartung");
    }
    public void setDefaultMOTD(String motd) {
        this.fileBuilder.setValue("MOTD.Default", motd);
        this.fileBuilder.save();
    }
    public String getDefaultMOTD() {
        return this.fileBuilder.getString("MOTD.Default");
    }
    public void setWartungMOTD(String motd) {
        this.fileBuilder.setValue("MOTD.Wartung", motd);
        this.fileBuilder.save();
    }
    public String getWartungMOTD() {
        return this.fileBuilder.getString("MOTD.Wartung");
    }
    public void setDefaultVersion(String version) {
        this.fileBuilder.setValue("Version.Default", version);
        this.fileBuilder.save();
    }
    public String getDefaultVersion() {
        return this.fileBuilder.getString("Version.Default");
    }
    public void setWartungVersion(String version) {
        this.fileBuilder.setValue("Version.Wartung", version);
        this.fileBuilder.save();
    }
    public String getWartungVersion() {
        return this.fileBuilder.getString("Version.Wartung");
    }
}
