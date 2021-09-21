package net.tiam.addictzone_proxy.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.*;
import net.tiam.addictzone_proxy.managers.SecurityManager;

import java.io.IOException;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(ServerConnectEvent e) throws IOException {
        String ip = e.getPlayer().getAddress().toString();
        String[] ips = ip.split(":");
        String iptrim = ips[0].replace('.', '_');
        int pCount = permittedTimePermission(e.getPlayer());
        int permCount = 0;
        String servername = MainClass.ServerName;
        String fullservermsg = "§9§lAddictZone §8➜ §3§lNetzwerk-Filter\n\n§7Der Server ist voll.\n§7Versuche es Später erneut.";
        String fullservermsgranked = "§9§lAddictZone §8➜ §3§lNetzwerk-Filter\n\n§7Der Server ist voll und alle zusaätzlichen §bRangslots.\n§7sind ebenfalls belegt. Versuche es Später erneut.";
        if (pCount == 0) {
            permCount = 1;
        } else {
            permCount = pCount;
        }
        if (!e.getPlayer().hasPermission(servername + ".wartung.bypass") && new SettingsManager().getBypass(e.getPlayer().getUniqueId().toString()) == false && new SettingsManager().getWartung() == true) {
            e.getPlayer().disconnect(MainClass.kickmessage);
            e.setCancelled(true);
            new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() - 1);
        }
        if (ProxyServer.getInstance().getOnlineCount() > new SettingsManager().getSlots() && !e.getPlayer().hasPermission(servername + ".slots.bypass.rank") && !e.getPlayer().hasPermission(servername + ".slots.bypass")) {
            e.getPlayer().disconnect(fullservermsg);
            e.setCancelled(true);
            new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() - 1);
        } else if (ProxyServer.getInstance().getOnlineCount() > new SettingsManager().getRankedSlots() && !e.getPlayer().hasPermission(servername + ".slots.bypass")) {
            e.getPlayer().disconnect(fullservermsgranked);
            e.setCancelled(true);
            new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() - 1);
        }
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
            new TablistManager().setTablist(all);

        String samecountmsg = "§9§lAddictZone §8➜ §3§lNetzwerk-Filter\n\n§7Du bist bereits mit §b" + new SecurityManager(iptrim).getIpCount() + " §7Accounts online.\n\nSolltest du der Meinung sein, dass dies ein\n§cFehler §7ist, melde dich im §6Support§7.\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §bhttps://AddictZone.net/Forum";
        new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() + 1);
        if (new SecurityManager(iptrim).getIpCount() > permCount) {
            e.getPlayer().disconnect(samecountmsg);
            e.setCancelled(true);
            new SecurityManager(iptrim).setIpCount(new SecurityManager(iptrim).getIpCount() - 1);
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
        System.out.println("lol");
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
    public static int permittedTimePermission(ProxiedPlayer player) {
        String servername = MainClass.ServerName;
        for (int i = 1000; i >= 0; i--) {
            if (player.hasPermission(servername + ".Proxy.Accounts.allowed." + i)) {
                return i;
            }
        }
        return 0;
    }
}
