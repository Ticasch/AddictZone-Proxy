package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;

public class PRCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = prefix + MainClass.NoPerm;
    String servername = MainClass.ServerName;
    public PRCMD() {
        super("Pr");
    }
    Thread sleep = new Thread();
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(servername + ".Proxy.Restart")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("confirm")) {
                    ProxyServer.getInstance().broadcast(prefix + "Netzwerk Neustart in §b60 §7Sekunden.");
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ProxyServer.getInstance().broadcast(prefix + "Netzwerk Neustart in §b30 §7Sekunden.");
                    try {
                        Thread.sleep(24000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ProxyServer.getInstance().broadcast(prefix + "Netzwerk Neustart in §b5 §7Sekunden.");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ProxyServer.getInstance().broadcast(prefix + "Netzwerk Neustart in §b4 §7Sekunden.");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ProxyServer.getInstance().broadcast(prefix + "Netzwerk Neustart in §b3 §7Sekunden.");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ProxyServer.getInstance().broadcast(prefix + "Netzwerk Neustart in §b2 §7Sekunden.");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ProxyServer.getInstance().broadcast(prefix + "Netzwerk Neustart in §b1 §7Sekunde.");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ProxyServer.getInstance().broadcast(prefix + "Netzwerk Neustart...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
                        all.disconnect(prefix + "§7Netzwerk Neustart");
                    ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "end");
                } else {
                    sender.sendMessage(prefix + "Benutze: §b/Pr confirm");
                }
            } else {
                sender.sendMessage(prefix + "Du musst diese Aktion Bestätigen!");

            }
        } else {
            sender.sendMessage(noperm);
        }
    }

}
