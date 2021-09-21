package net.tiam.addictzone_proxy.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.AccountManager;
import net.tiam.addictzone_proxy.managers.SecurityManager;
import net.tiam.addictzone_proxy.managers.TablistManager;
import net.tiam.addictzone_proxy.managers.WartungManager;

import java.io.IOException;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(ServerConnectEvent e) throws IOException {
        int pCount = permittedTimePermission(e.getPlayer());
        int permCount = 0;
        if (pCount == 0) {
            permCount = 1;
        } else {
            permCount = pCount;
        }
        int OnlineCount = new AccountManager(e.getPlayer().getAddress().toString(), e.getPlayer().getUniqueId().toString()).getSameCount();
        if (!e.getPlayer().hasPermission("addictzone.wartung.bypass") && new WartungManager(e.getPlayer().getUniqueId().toString()).getBypass() == false && new WartungManager(e.getPlayer().getUniqueId().toString()).getWartung() == true) {
            e.getPlayer().disconnect(MainClass.kickmessage);
            e.setCancelled(true);
        }
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
            new TablistManager().setTablist(all);
        String ip = e.getPlayer().getAddress().toString();
        String[] ips = ip.split(":");
        String iptrim = ips[0].replace('.', '_');
        String samecountmsg = "§9§lAddictZone §8➜ §3§lNetzwerk-Filter\n\n§7Du bist bereits mit §b" + new SecurityManager(iptrim).getIpCount() + " §7Accounts online.\n\nSolltest du der Meinung sein, dass dies ein\n§cFehler §7ist, melde dich im §6Support§7.\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §7https://AddictZone.net/Forum";
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
        String motd = "§9§lAddictZone §7- §c§lAufbauphase\n§6§lBeta-Release: §b01.12.2021";
        ping.setPlayers(new ServerPing.Players(512, ProxyServer.getInstance().getOnlineCount(), null));
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
