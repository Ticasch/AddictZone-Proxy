package net.tiam.addictzone_proxy;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.tiam.addictzone_proxy.commands.*;

import java.util.Collection;

public final class MainClass extends Plugin implements Listener {

    public static String ServerName = "AddictZone";
    public static String Prefix = "§9§lAddictZone §8➜ §7";
    public static String NoPerm = "§7Dazu hast du keine Rechte!";
    public static String Line = "§8§m--------------------------------------------------";
    public boolean wartung = true;
    private Collection<? extends ProxiedPlayer> onlineplayer;
    String kickmessage = "§9§lAddictZone §7- §6§lWartungen\n\n§7Unser Netzwerk befindet sich in §6Wartungsarbeiten§7.\n§7Wir werden bald wieder Erreichbar sein.\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §bhttps://AddictZone.net/Forum";

    public void onEnable() {
        registerCommands();
    }

    @Override
    public void onDisable() {
    }

    public void registerCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new LobbyCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new AdminChatCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PcCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PopCMD());
        //ProxyServer.getInstance().getPluginManager().registerCommand(this, new TeamChatCMD());
    }
    @EventHandler
    public void onLogin(ServerConnectEvent e) {
        if (this.wartung && !e.getPlayer().hasPermission("addictzone.wartung.bypass")) {
            e.setCancelled(true);
            e.getPlayer()
                    .disconnect(kickmessage);
        } 
    }
}

