package net.tiam.addictzone_proxy.model;

import java.util.List;
import java.util.Map;

public class History {

    private int actuallyCountAll;
    private int actuallyCountBans;
    private int actuallyCountMutes;
    private Map<Integer, HistoryEntry> histories;

    public History(){

    }

    public History(int actuallyCountAll, int actuallyCountBans, int actuallyCountMutes, Map<Integer, HistoryEntry> histories) {
        this.actuallyCountAll = actuallyCountAll;
        this.actuallyCountBans = actuallyCountBans;
        this.actuallyCountMutes = actuallyCountMutes;
        this.histories = histories;
    }

    public int getActuallyCountAll() {
        return actuallyCountAll;
    }

    public void setActuallyCountAll(int actuallyCountAll) {
        this.actuallyCountAll = actuallyCountAll;
    }

    public int getActuallyCountBans() {
        return actuallyCountBans;
    }

    public void setActuallyCountBans(int actuallyCountBans) {
        this.actuallyCountBans = actuallyCountBans;
    }

    public int getActuallyCountMutes() {
        return actuallyCountMutes;
    }

    public void setActuallyCountMutes(int actuallyCountMutes) {
        this.actuallyCountMutes = actuallyCountMutes;
    }

    public Map<Integer, HistoryEntry> getHistories() {
        return histories;
    }

    public void setHistories(Map<Integer, HistoryEntry> histories) {
        this.histories = histories;
    }
}
