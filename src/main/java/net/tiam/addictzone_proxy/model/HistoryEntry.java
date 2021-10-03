package net.tiam.addictzone_proxy.model;

public class HistoryEntry {

    private int id;
    private HistoryType historyType;
    private boolean taken;
    private String takenBy, name, ip, reason, expiry, banner;
    private Long expiryLong, bandate;

    public HistoryEntry() {
    }

    public HistoryEntry(Integer id, HistoryType historyType, boolean taken, String takenBy, String name, String ip, String reason, String expiry, String banner, Long expiryLong, Long bandate) {
        this.id = id;
        this.historyType = historyType;
        this.taken = taken;
        this.takenBy = takenBy;
        this.name = name;
        this.ip = ip;
        this.reason = reason;
        this.expiry = expiry;
        this.banner = banner;
        this.expiryLong = expiryLong;
        this.bandate = bandate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HistoryType getHistoryType() {
        return historyType;
    }

    public void setHistoryType(HistoryType historyType) {
        this.historyType = historyType;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Long getExpiryLong() {
        return expiryLong;
    }

    public void setExpiryLong(Long expiryLong) {
        this.expiryLong = expiryLong;
    }

    public Long getBandate() {
        return bandate;
    }

    public void setBandate(Long bandate) {
        this.bandate = bandate;
    }
}
