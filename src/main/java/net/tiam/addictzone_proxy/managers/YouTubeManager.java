package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.commands.YouTuberCMD;
import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;

public class YouTubeManager {
    private String uuid;

    private FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "YouTube.yml");

    public YouTubeManager(String uuid) throws IOException {
        this.uuid = uuid;
    }
    public void setYouTuber(boolean status, String link) {
        this.fileBuilder.setValue(this.uuid + ".Status", true);
        this.fileBuilder.setValue(this.uuid + ".Link" , link);
        this.fileBuilder.save();
    }
    public String getLink() {
        return this.fileBuilder.getString(this.uuid + ".Link");
    }
    public boolean getYouTuber() {
        if (this.fileBuilder.getString(this.uuid) == null)
            return false;
        return this.fileBuilder.getBoolean(this.uuid + ".Status");
    }
}
