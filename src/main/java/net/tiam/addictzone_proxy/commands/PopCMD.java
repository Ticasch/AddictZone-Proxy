package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;

import java.io.Console;

public class PopCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = prefix + MainClass.NoPerm;
    String servername = MainClass.ServerName;
    String SENDER;
    public PopCMD() {
        super("Pop");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        CommandSender c = (CommandSender) sender;
        if (c instanceof ProxiedPlayer) {
            SENDER = c.getName();
        } else {
            SENDER = "AddictZone";
        }
        if (c.hasPermission(servername + ".Pop")) {

            if (args.length == 1) {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                if (target.hasPermission("*")) {
                    c.sendMessage(prefix + "Dieser Spieler hat bereits alle Bungee-Rechte.");
                } else {
                    c.sendMessage(prefix + "Du hast dem Spieler §b" + target.getName() + " §7alle Rechte zugeteilt");
                    target.sendMessage(prefix + "Dir wurden von §b" + SENDER + " §7alle Bungee-Rechte zugeteilt.");
                    ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "lpb user " + target + " permission set *");
                }
            } else {
                c.sendMessage(prefix + "Benutze: §b/Pop §7<§bSpieler§7>");
            }
        } else {
            c.sendMessage(noperm);
        }
    }
}
