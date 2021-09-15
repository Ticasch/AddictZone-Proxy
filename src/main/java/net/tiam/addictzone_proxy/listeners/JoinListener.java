package net.tiam.addictzone_proxy.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.tiam.addictzone_proxy.MainClass;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(ServerConnectEvent e) {
        if (!e.getPlayer().hasPermission("addictzone.wartung.bypass")) {
            e.getPlayer().disconnect(MainClass.kickmessage);
            e.setCancelled(true);
        }
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
}
