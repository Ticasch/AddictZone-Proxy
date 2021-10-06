package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.SettingsManager;

import java.io.IOException;

public class VersionCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    public VersionCMD() {
        super("Version");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if (!c.hasPermission(servername + ".command.motd")) {
            c.sendMessage(noperm);
            return;
        }
        if (args.length >= 2) {
            String msg = "";
            for (int i = 1; i != args.length; i++)
                msg = msg + args[i] + " ";
            msg = msg.replace("&", "§");
            if (args[0].equalsIgnoreCase("normal")) {
                try {
                    new SettingsManager().setDefaultVersion(msg);
                    c.sendMessage(prefix + "Du hast die Standart-Version Anzeige gesetzt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (args[0].equalsIgnoreCase("wartung")) {
                try {
                    new SettingsManager().setWartungVersion(msg);
                    c.sendMessage(prefix + "Du hast die Wartungs-Version Anzeige gesetzt.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                c.sendMessage(prefix + "Benutze: §b/Version §7<§bNormal§7|§bWartung§7> <§bText§7>");
            }
        } else {
            c.sendMessage(prefix + "Benutze: §b/Version §7<§bNormal§7|§bWartung§7> <§bText§7>");
        }
    }
}
