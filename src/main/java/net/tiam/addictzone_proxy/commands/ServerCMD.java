package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;

public class ServerCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    public ServerCMD() {
        super("server");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (p.hasPermission(servername + ".Server")) {
            String server = String.valueOf(args[0]);
        } else {
            p.sendMessage(noperm);
        }
    }
}
