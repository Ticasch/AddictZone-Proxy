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
}
