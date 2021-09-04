package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ConnectedPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;

public class TeamChatCMD extends Command {
    String servername = MainClass.ServerName;
    String prefix = MainClass.Prefix;
    String noperm = MainClass.Prefix + MainClass.NoPerm;
    String SENDER = servername;
    public TeamChatCMD() {
        super("Tc");
    }
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (p.hasPermission(servername + ".TC")) {
                if (args.length >= 1) {

                    String message = "";
                    for (int i = 0; i != args.length; i++)
                        message = message + args[i] + " ";
                        if (p.hasPermission(servername + ".Tc.Color")) {
                            message = ChatColor.translateAlternateColorCodes('&', message);
                        }
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            if (all.hasPermission(servername + ".Tc")) {
                                all.sendMessage("§6§l@TEAM §8[§b" + p.getServer().getInfo().getName() + "§8] §7" + p.getName() + " §8➜ §7" + message);
                            }
                        }
                        ProxyServer.getInstance().getConsole().sendMessage("§6§l@TEAM §8[§b" + p.getServer().getInfo().getName() + "§8] §7" + p.getName() + " §8➜ §7" + message);
                } else {
                    p.sendMessage(prefix + "Benutze: §b/Tc §7<§bText§7>");
                }
            } else {
                p.sendMessage(noperm);
            }
        } else {
            if (args.length >= 1) {
                String message = "";
                for (int i = 0; i != args.length; i++)
                    message = message + args[i] + " ";
                    if (sender.hasPermission(servername + ".Tc.Color")) {
                        message = ChatColor.translateAlternateColorCodes('&', message);
                    }
                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        if (all.hasPermission(servername + ".Tc")) {
                            all.sendMessage("§6§l@TEAM §8[§bGlobal§8] §7" + SENDER + " §8➜ §7" + message);
                        }
                    }
                    ProxyServer.getInstance().getConsole().sendMessage("§6§l@TEAM §8[§bGlobal§8] §7" + SENDER + " §8➜ §7" + message);
            } else {
                sender.sendMessage(prefix + "Benutze: §b/Tc §7<§bText§7>");
            }
        }
    }
}
