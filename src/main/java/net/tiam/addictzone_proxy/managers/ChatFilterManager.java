package net.tiam.addictzone_proxy.managers;

import net.tiam.addictzone_proxy.model.History;
import net.tiam.addictzone_proxy.model.HistoryEntry;
import net.tiam.addictzone_proxy.utilities.FileBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatFilterManager {

    private FileBuilder fileBuilder = new FileBuilder("plugins/AddictZone-Proxy", "ChatFilter.yml");

    public ChatFilterManager() throws IOException {
    }
    public ArrayList<String> getList() {
        ArrayList<String> list = new ArrayList<>(this.fileBuilder.getKeys());
        return list;
    }
}
