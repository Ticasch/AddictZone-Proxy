package net.tiam.addictzone_proxy.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class UnMuteCMD extends Command {
    String prefix = MainClass.Prefix;
    String line = MainClass.Line;
    String servername = MainClass.ServerName;
    String noperm = prefix + MainClass.NoPerm;
    String UNMUTER;

    public UnMuteCMD(){
        super("unmute");
    }

    @Override
    public void execute(CommandSender c, String[] args) {
        if(!(c.hasPermission(servername + ".Unmute"))) {
            c.sendMessage(noperm);
            return;
        }
        if (c instanceof ProxiedPlayer) {
            UNMUTER = c.getName();
        } else {
            UNMUTER = servername;
        }
        if (args.length == 1) {
            ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
            String target = String.valueOf(args[0]);
            UUID targetUUID = getUUIDFromName(target);
            String ip = "";
            if (t == null) {
                try {
                    if (new IPManager(targetUUID.toString(), target).getIP() == null) {
                        ip = "0.0.0.0";
                    } else {
                        ip = new IPManager(targetUUID.toString(), target).getIP();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ip = t.getAddress().getHostName().toString();
            }
            if (targetUUID == null) {
                c.sendMessage(prefix + "Dieser Spieler existiert nicht.");
                return;
            }
            String[] ips = ip.split(":");
            String iptrim = ips[0].replace('.', '_');
            if (targetUUID == null) {
                c.sendMessage(prefix + "Dieser Spieler ist nicht registriert.");
            } else {
                try {
                    if (new MuteManager(target, targetUUID.toString()).getMuted() == false) {
                        c.sendMessage(prefix + "Dieser Spieler ist nicht gemutet.");
                    } else {
                        int actuallyCount = 0 + new HistoryManager(target, targetUUID.toString()).getActuallyCount();
                        c.sendMessage(prefix + "§7Du hast den Spieler §b" + target + " §7erfolgreich entmutet.");
                        new MuteManager(target, targetUUID.toString()).setMutedStatus(false);
                        new AutoBanManager().setIpStatusMuted(iptrim, false);
                        new HistoryManager(target, targetUUID.toString()).settaken(true, UNMUTER, actuallyCount);
                        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            if(all.hasPermission(servername + ".ban.Notify")) {
                                all.sendMessage(line);
                                all.sendMessage(prefix + "§7Art: §a§lUNMUTE");
                                all.sendMessage(prefix + "§7Name: §b" + target);
                                all.sendMessage(prefix + "§7Von: §b" + c.getName());
                                all.sendMessage(line);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            c.sendMessage(prefix + "Benutze: §b/Unban §7<§bSpieler§7>");
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
