package net.tiam.addictzone_proxy;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.tiam.addictzone_proxy.commands.*;

public final class MainClass extends Plugin {

    public static String ServerName = "AddictZone";
    public static String Prefix = "§9§lAddictZone §8➜ §7";
    public static String NoPerm = "§7Dazu hast du keine Rechte!";
    public static String Line = "§8§m--------------------------------------------------";
    @Override
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
}
