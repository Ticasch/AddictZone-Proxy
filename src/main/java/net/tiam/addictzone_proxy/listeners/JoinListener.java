package net.tiam.addictzone_proxy.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(ServerConnectEvent e) throws IOException {
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(e.getPlayer().getUniqueId());
        String ip = target.getAddress().getHostName().toString();
        String[] ips = ip.split(":");
        String iptrim = ips[0].replace('.', '_');
        String Ban_IP = new BanManager(target.toString(), target.getUniqueId().toString()).getIp();
        String Ban_Banner = new BanManager(target.getName(), target.getUniqueId().toString()).getBanner();
        String Ban_Reason = new BanManager(target.getName(), target.getUniqueId().toString()).getReason();
        String Ban_Expiry = "";
        if (new BanManager(target.getName(), target.getUniqueId().toString()).getPermanently() == true) {
            Ban_Expiry = "Permanent";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            format.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
            Ban_Expiry = format.format(new BanManager(target.getName(), target.getUniqueId().toString()).getExpiryLong());
        }
        boolean banned = new BanManager(target.getName(), target.getUniqueId().toString()).getBanned();
        boolean muted = new MuteManager(target.getName(), target.getUniqueId().toString()).getMuted();
        String servername = MainClass.ServerName;
        String PBanKickMsg = "§9§lAddictZone §8➜ §4§lGEBANNT\n\n§7Von: §b" + Ban_Banner + "\n§7Grund: §b" + Ban_Reason + "\n§7Dauer: §b" + Ban_Expiry + "\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §bhttps://AddictZone.net/Forum";
        String fullservermsg = "§9§lAddictZone §8➜ §3§lNetzwerk-Filter\n\n§7Der Server ist voll.\n§7Versuche es Später erneut.";
        String fullservermsgranked = "§9§lAddictZone §8➜ §3§lNetzwerk-Filter\n\n§7Der Server ist voll und alle zusaätzlichen §bRangslots.\n§7sind ebenfalls belegt. Versuche es Später erneut.";
        int accounts = 0;
        ProxiedPlayer player = e.getPlayer();
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            if (player.getAddress().getHostName().equals(all.getAddress().getHostName())) {
                if (all != player) {
                    accounts++;
                    if (accounts >= permittedAccountsPermission(target.getUniqueId())) {
                        String samecountmsg = "§9§lAddictZone §8➜ §3§lNetzwerk-Filter\n\n§7Du bist bereits mit §b" + accounts + " §7Account(s) online.\n\nSolltest du der Meinung sein, dass dies ein\n§cFehler §7ist, melde dich im §6Support§7.\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §bhttps://AddictZone.net/Forum";
                        e.getPlayer().disconnect(samecountmsg);
                        e.setCancelled(true);
                    }
                }
            }
        }
        if (!target.hasPermission(servername + ".wartung.bypass") && new SettingsManager().getBypass(target.getUniqueId().toString()) == false && new SettingsManager().getWartung() == true) {
            e.setCancelled(true);
            target.disconnect(MainClass.kickmessage);
        }
        if (ProxyServer.getInstance().getOnlineCount() > new SettingsManager().getSlots() && !target.hasPermission(servername + ".slots.bypass.rank") && !target.hasPermission(servername + ".slots.bypass")) {
            e.setCancelled(true);
            target.disconnect(fullservermsg);
        } else if (ProxyServer.getInstance().getOnlineCount() > new SettingsManager().getRankedSlots() && !target.hasPermission(servername + ".slots.bypass")) {
            e.setCancelled(true);
            target.disconnect(fullservermsgranked);
        }
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
            new TablistManager().setTablist(all);
        new IPManager(target.getUniqueId().toString(), target.getName()).setName();
        new IPManager(target.getUniqueId().toString(), target.getName()).setIP(iptrim.replace("/", ""));
        if (banned == true) {
            if (new BanManager(target.getName(), target.getUniqueId().toString()).getExpiryLong() <= System.currentTimeMillis() && new BanManager(target.getName(), target.getUniqueId().toString()).getExpiryLong() > 0 && new BanManager(target.getName(), target.getUniqueId().toString()).getPermanently() == false) {
                new BanManager(target.getName(), target.getUniqueId().toString()).setBannedStatus(false);
                new AutoBanManager().setIPStatusBanned(iptrim.replace("/", ""), false);
                new HistoryManager(target.getName(), target.getUniqueId().toString()).settaken(true, servername + "§7(§cAutomatisch§7)", new HistoryManager(target.getName(), target.getUniqueId().toString()).getActuallyCount());
                return;
            }
            new AutoBanManager().setIPStatusBanned(iptrim.replace("/", ""), true);
            e.setCancelled(true);
            target.disconnect(PBanKickMsg);
            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
                new TablistManager().setTablist(all);
        }
        if (new AutoBanManager().getIPStatusBanned(iptrim.replace("/", ""))) {
            if (new BanManager(target.getName(), target.getUniqueId().toString()).getBanned() == false)
                ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "Pban " + target.getName() + " Bannumgehung §7(§cAccount Liste§7)");
            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
                new TablistManager().setTablist(all);
        }
        if (new AutoBanManager().getIPStatusMuted(iptrim.replace("/", ""))) {
            if (new BanManager(target.getName(), target.getUniqueId().toString()).getBanned() == false) {
                if (new MuteManager(target.getName(), target.getUniqueId().toString()).getMuted() == false) {
                    if (new AutoBanManager().getIPStatusBanned(iptrim.replace("/", "")) == false) {
                        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "Pban " + target.getName() + " Muteumgehung §7(§cAccount Liste§7)");
                        System.out.println("Muteumgehung");
                    }
                }
            }
            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
                new TablistManager().setTablist(all);
        }
    }
    @EventHandler
    public void onQuit(ServerDisconnectEvent e) throws IOException {
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            new TablistManager().setTablist(all);
        }
        String ip = e.getPlayer().getAddress().toString();
        String[] ips = ip.split(":");
        String iptrim = ips[0].replace('.', '_');
    }
    @EventHandler
    public void onKick(ServerKickEvent e) throws IOException {
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            new TablistManager().setTablist(all);
        }
        String ip = e.getPlayer().getAddress().toString();
        String[] ips = ip.split(":");
        String iptrim = ips[0].replace('.', '_');
    }
    @EventHandler
    public void onPing (ProxyPingEvent e) {
        ServerPing ping = e.getResponse();
        int slots = 0;
        try {
            slots = new SettingsManager().getSlots();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String motd = "§9§lAddictZone §7- §c§lAufbauphase\n§6§lBeta-Release: §b01.12.2021";
        ping.setPlayers(new ServerPing.Players(slots, ProxyServer.getInstance().getOnlineCount(), null));
        ping.setVersion(new ServerPing.Protocol("§6§lWartungen", ProxyServer.getInstance().getOnlineCount()));
        ping.setDescription(motd);
        e.setResponse(ping);
    }
    public static int permittedAccountsPermission(UUID player) {
        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);
        String servername = MainClass.ServerName;
        for (int i = 1000; i >= 0; i--) {
            if (p.hasPermission(servername + ".Proxy.Accounts.allowed." + i)) {
                return i;
            }
        }
        return 0;
    }
}
