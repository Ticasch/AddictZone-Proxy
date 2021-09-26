package net.tiam.addictzone_proxy;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.tiam.addictzone_proxy.commands.*;
import net.tiam.addictzone_proxy.listeners.AsyncPlayerChatListener;
import net.tiam.addictzone_proxy.listeners.JoinListener;
import net.tiam.addictzone_proxy.managers.BanManager;
import net.tiam.addictzone_proxy.managers.TablistManager;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public final class MainClass extends Plugin {

    public static String ServerName = "AddictZone";
    public static String Prefix = "§9§lAddictZone §8➜ §7";
    public static String NoPerm = "§7Dazu hast du keine Rechte!";
    public static String Line = "§8§m--------------------------------------------------";
    public static String kickmessage = "§9§lAddictZone §7- §6§lWartungen\n\n§7Unser Netzwerk befindet sich derzeit in der §cAufbauphase§7.\n§7Bei Fragen wende dich gerne an unseren §6Support§7.\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §bhttps://AddictZone.net/Forum";

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
        ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PRCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SetSlotsCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PBanCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PMuteCMD());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new AsyncPlayerChatListener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TBanCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new YouTuberCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CheckCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HistoryCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnBanCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnMuteCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TMuteCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PSudoCMD());
        ProxyServer.getInstance().getScheduler().schedule(this, () -> {
            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                new TablistManager().setTablist(all);
            }
        }, 0, 1, TimeUnit.SECONDS);
        //ProxyServer.getInstance().getPluginManager().registerCommand(this, new TeamChatCMD());
    }

}

