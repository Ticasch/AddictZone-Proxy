package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.SettingsManager;

import java.io.IOException;

public class SetSlotsCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    public SetSlotsCMD() {
        super("setslots");
    }

    @Override
    public void execute(CommandSender c, String[] args) {
        if (c.hasPermission(servername + ".setslots")) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("normal")) {
                    int oldSlots = 0;
                    int newSlots = 0;
                    if (!isDouble(args[1])) {
                        c.sendMessage(prefix + "Du musst eine Zahl angeben.");
                    } else {
                        newSlots = Integer.parseInt(args[1]);
                        try {
                            oldSlots = new SettingsManager().getSlots();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        c.sendMessage(prefix + "Du hast die Serverslots von §b" + oldSlots + " §7auf §b" + newSlots + " §7gesetzt.");
                        try {
                            new SettingsManager().setSlots(newSlots);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (args[0].equalsIgnoreCase("ranked")) {
                    int oldSlots = 0;
                    int newSlots = 0;
                    if (!isDouble(args[1])) {
                        c.sendMessage(prefix + "Du musst eine Zahl angeben.");
                    } else {
                        newSlots = Integer.parseInt(args[1]);
                        try {
                            oldSlots = new SettingsManager().getRankedSlots();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        c.sendMessage(prefix + "Du hast die Ranked-Serverslots von §b" + oldSlots + " §7auf §b" + newSlots + " §7gesetzt.");
                        try {
                            new SettingsManager().setRankedSlots(newSlots);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    c.sendMessage(prefix + "Benutze: §b/Setslots §7<§bnormal§7|§branked§7> §7<§bSlots§7>");
                }
            } else {
                c.sendMessage(prefix + "Benutze: §b/Setslots §7<§bnormal§7|§branked§7> §7<§bSlots§7>");
            }
        } else {
            c.sendMessage(noperm);
        }
    }

    public static boolean isDouble(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException|NullPointerException nfe) {
            return false;
        }
        return true;
    }
}
