package net.tiam.addictzone_proxy.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.*;
import net.tiam.addictzone_proxy.managers.SecurityManager;

import java.io.IOException;
import java.util.UUID;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(ServerConnectEvent e) throws IOException {
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(e.getPlayer().getUniqueId());
        String ip = target.getAddress().toString();
        String[] ips = ip.split(":");
        String iptrim = ips[0].replace('.', '_');
        String Ban_IP = new BanManager(target.toString(), target.getUniqueId().toString()).getIp();
        String Ban_Banner = new BanManager(target.getName(), target.getUniqueId().toString()).getBanner();
        String Ban_Reason = new BanManager(target.getName(), target.getUniqueId().toString()).getReason();
        String Ban_Expiry = "";
        if (new BanManager(target.getName(), target.getUniqueId().toString()).getPermanently() == true) {
            Ban_Expiry = "Permanent";
        }
        boolean banned = new BanManager(target.getName(), target.getUniqueId().toString()).getBanned();
        boolean muted = new MuteManager(target.getName(), target.getUniqueId().toString()).getMuted();
        int pCount = permittedTimePermission(target.getUniqueId());
        int permCount = 0;
        String servername = MainClass.ServerName;
        String PBanKickMsg = "§9§lAddictZone §8➜ §4§lGEBANNT\n\n§7Von: §b" + Ban_Banner + "\n§7Grund: §b" + Ban_Reason + "\n§7Dauer: §b" + Ban_Expiry + "\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §bhttps://AddictZone.net/Forum";
        String fullservermsg = "§9§lAddictZone §8➜ §3§lNetzwerk-Filter\n\n§7Der Server ist voll.\n§7Versuche es Später erneut.";
        String fullservermsgranked = "§9§lAddictZone §8➜ §3§lNetzwerk-Filter\n\n§7Der Server ist voll und alle zusaätzlichen §bRangslots.\n§7sind ebenfalls belegt. Versuche es Später erneut.";
        if (pCount == 0) {
            permCount = 1;
        } else {
            permCount = pCount;
        }
        if (!target.hasPermission(servername + ".wartung.bypass") && new SettingsManager().getBypass(target.getUniqueId().toString()) == false && new SettingsManager().getWartung() == true) {
            e.setCancelled(true);
            target.disconnect(MainClass.kickmessage);
            new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() - 1);
        }
        if (ProxyServer.getInstance().getOnlineCount() > new SettingsManager().getSlots() && !target.hasPermission(servername + ".slots.bypass.rank") && !target.hasPermission(servername + ".slots.bypass")) {
            e.setCancelled(true);
            target.disconnect(fullservermsg);
            new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() - 1);
        } else if (ProxyServer.getInstance().getOnlineCount() > new SettingsManager().getRankedSlots() && !target.hasPermission(servername + ".slots.bypass")) {
            e.setCancelled(true);
            target.disconnect(fullservermsgranked);
            new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() - 1);
        }
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
            new TablistManager().setTablist(all);
        new IPManager(target.getUniqueId().toString(), target.getName()).setName();
        new IPManager(target.getUniqueId().toString(), target.getName()).setIP(iptrim.replace("/", ""));
        System.out.println("§9§lLog1");
        String samecountmsg = "§9§lAddictZone §8➜ §3§lNetzwerk-Filter\n\n§7Du bist bereits mit §b" + new SecurityManager(iptrim).getIpCount() + " §7Accounts online.\n\nSolltest du der Meinung sein, dass dies ein\n§cFehler §7ist, melde dich im §6Support§7.\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §bhttps://AddictZone.net/Forum";
        new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() + 1);
        if (new SecurityManager(iptrim).getIpCount() > permCount) {
            e.setCancelled(true);
            target.disconnect(samecountmsg);
            new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() - 1);
            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
                new TablistManager().setTablist(all);
        }
        if (banned == true) {
            new AutoBanManager().setIPStatusBanned(iptrim.replace("/", ""), true);
            e.setCancelled(true);
            target.disconnect(PBanKickMsg);
            new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() - 1);
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
        new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() - 1);
    }
    @EventHandler
    public void onKick(ServerKickEvent e) throws IOException {
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            new TablistManager().setTablist(all);
        }
        String ip = e.getPlayer().getAddress().toString();
        String[] ips = ip.split(":");
        String iptrim = ips[0].replace('.', '_');
        new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount());
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
    public static int permittedTimePermission(UUID player) {
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
