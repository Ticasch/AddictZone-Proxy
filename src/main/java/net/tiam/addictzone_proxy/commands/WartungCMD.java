package net.tiam.addictzone_proxy.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.SettingsManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

public class WartungCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = prefix + MainClass.NoPerm;
    String servername = MainClass.ServerName;
    public WartungCMD() {
        super("wartung");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if (!(c.hasPermission(servername + ".command.wartung"))) {
            c.sendMessage(noperm);
            return;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("Status")) {
                if (!c.hasPermission(servername + ".Wartung.status")) {
                    c.sendMessage(prefix + "Du hast keine Rechte, den Wartungs-Status zu verändern.");
                    return;
                }
                try {
                    if (new SettingsManager().getWartung() == false) {
                        new SettingsManager().setWartung(true);
                        c.sendMessage(prefix + "Du hast den Wartungsmodus aktiviert.");
                    } else {
                        new SettingsManager().setWartung(false);
                        c.sendMessage(prefix + "Du hast den Wartungsmodus deaktiviert.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (args[0].equalsIgnoreCase("bypass")) {
                c.sendMessage(prefix + "Benutze: §b/Wartung Bypass §7<§badd§7|§bremove§7> <§bSpieler§7>");
            } else {
                c.sendMessage(prefix + "Benutze: §b/Wartung Status");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("Bypass")) {
                if (!c.hasPermission(servername + ".Wartung.Bypass")) {
                    c.sendMessage(prefix + "Du hast keine Rechte, die Watungs-Bypass Liste zu editieren.");
                    return;
                }
                ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[2]);
                String target = String.valueOf(args[2]);
                UUID targetUUID = getUUIDFromName(target);
                if (targetUUID == null) {
                    c.sendMessage(prefix + "Dieser Spieler existiert nicht.");
                    return;
                }
                try {
                    if (args[1].equalsIgnoreCase("add")) {
                        if (new SettingsManager().getBypass(targetUUID.toString()) == true) {
                            c.sendMessage(prefix + "Dieser Speielr ist bereits auf der Wartungs-Bapass Liste.");
                            return;
                        }
                        new SettingsManager().setBypass(targetUUID.toString(), true);
                        c.sendMessage(prefix + "Du hast §b" + target + " §7zur Wartung-Bypass Liste hinzugefügt.");
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        if (new SettingsManager().getBypass(targetUUID.toString()) == false) {
                            c.sendMessage(prefix + "Dieser Spieler ist nicht auf der Wartungs-Bypass Liste.");
                            return;
                        }
                        new SettingsManager().setBypass(targetUUID.toString(), false);
                        c.sendMessage(prefix + "Du hast §b" + target + " §7von der Wartungs-Bypass Liste entfernt.");
                    } else {
                        c.sendMessage(prefix + "Benutze: §b/Wartung Bypass §7<§badd§7|§bremove§7> <§bSpieler§7>");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                c.sendMessage(prefix + "Benutze: §b/Wartung §7<§bStatus§7|§bBypass§7> <§bSpieler§7>");
            }
        } else {
            c.sendMessage(prefix + "Benutze: §b/Wartung §7<§bStatus§7|§bBypass§7> <§bSpieler§7>");
        }
    }
    public static UUID getUUIDFromName(String name){
        try {
            Object o = new JsonParser().parse(new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream())));
            if (o instanceof JsonObject)
                return UUID.fromString(((JsonObject) o).get("id").getAsString().replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)",
                        "$1-$2-$3-$4-$5"));
        } catch (IOException ignored) {
            return null;
        }
        return null;
    }
}
