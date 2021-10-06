package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;

public class PSudoCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    public PSudoCMD() {
        super("psudo");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if (c.hasPermission(servername + ".psudo")) {
            if (args.length >= 2) {
                ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
                if (t == null) {
                    c.sendMessage(prefix + "Dieser spieler ist nicht online.");
                    return;
                }
                String msg = "";
                for (int i = 1; i != args.length; i++)
                    msg = msg + args[i] + " ";
                ProxyServer.getInstance().getPluginManager().dispatchCommand(t, msg.replace("/", ""));
            } else {
                c.sendMessage(prefix + "Benutze: §b/Psudo §7<§bSpieler§7> <§bText§7>");
            }
        } else {
            c.sendMessage(noperm);
        }
    }
}
