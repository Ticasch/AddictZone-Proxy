package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.tiam.addictzone_proxy.MainClass;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PcCMD extends Command {
    public PcCMD() {
        super("Pc");
    }
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    @Override
    public void execute(CommandSender sender, String[] args) {
        CommandSender p = (CommandSender) sender;
        if (p.hasPermission(servername + ".Bungeecast")) {
            if (args.length >= 1) {
                String message = "";
                for (int i = 0; i != args.length; i++)
                    message = message + args[i] + " ";
                message = message.replace('&', '§');
                ProxyServer.getInstance().broadcast(prefix);
                ProxyServer.getInstance().broadcast(prefix + message);
                ProxyServer.getInstance().broadcast(prefix);
            } else {
                p.sendMessage(prefix + "Benutze: §b/Pc §7<§bTest§7>");
            }
        } else {
            p.sendMessage(noperm);
        }
    }
}
