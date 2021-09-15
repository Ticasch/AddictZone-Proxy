package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PRCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = prefix + MainClass.NoPerm;
    String servername = MainClass.ServerName;
    public PRCMD() {
        super("Pr");
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(servername + "Proxy.Restart")) {
            ProxyServer.getInstance().broadcast(prefix + "Netzwerk Neustart in:");
            int i = 0;
            int a = 5;
            while (i <=5) {
                int b = a;
                ProxyServer.getInstance().getScheduler().schedule(new MainClass(), () -> {
                    ProxyServer.getInstance().broadcast(prefix + "§b" + b);
                }, 0L, 1, TimeUnit.SECONDS);
                i++;
                a--;
            } for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
                all.disconnect(prefix + "§7Netzwerk neustart");
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "end");

        } else {
            sender.sendMessage(noperm);
        }
    }
}
