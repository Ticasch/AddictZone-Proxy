package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ConnectedPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;

public class AdminChatCMD extends Command {
    String servername = MainClass.ServerName;
    String prefix = MainClass.Prefix;
    String noperm = MainClass.Prefix + MainClass.NoPerm;
    String SENDER = servername;
    public AdminChatCMD() {
    super("Ac");
    }
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (p.hasPermission(servername + ".AC")) {
                if (args.length >= 1) {

                        String message = "";
                        for (int i = 0; i != args.length; i++)
                            message = message + args[i] + " ";
                        if (p.hasPermission(servername + ".Ac.Color")) {
                            message = ChatColor.translateAlternateColorCodes('&', message);
                        }
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            if (all.hasPermission(servername + ".Ac")) {
                                all.sendMessage("§c§l@ADMIN §8[§b" + p.getServer().getInfo().getName() + "§8] §7" + p.getName() + " §8➜ §7" + message);
                            }
                        }
                        ProxyServer.getInstance().getConsole().sendMessage("§c§l@ADMIN §8[§b" + p.getServer().getInfo().getName() + "§8] §7" + p.getName() + " §8➜ §7" + message);

                } else {
                    p.sendMessage(prefix + "Benutze: §b/Ac §7<§bText§7>");
                }
            } else {
                p.sendMessage(noperm);
            }
        } else {
            if (args.length >= 1) {

                    String message = "";
                    for (int i = 0; i != args.length; i++)
                        message = message + args[i] + " ";
                    if (sender.hasPermission(servername + ".Ac.Color")) {
                        message = ChatColor.translateAlternateColorCodes('&', message);
                    }
                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        if (all.hasPermission(servername + ".Ac")) {
                            all.sendMessage("§c§l@ADMIN §8[§bGlobal§8] §7" + SENDER + " §8➜ §7" + message);
                        }
                    }
                    ProxyServer.getInstance().getConsole().sendMessage("§c§l@ADMIN §8[§bGlobal§8] §7" + SENDER + " §8➜ §7" + message);

            } else {
                sender.sendMessage(prefix + "Benutze: §b/Ac §7<§bText§7>");
            }
        }
    }
}
