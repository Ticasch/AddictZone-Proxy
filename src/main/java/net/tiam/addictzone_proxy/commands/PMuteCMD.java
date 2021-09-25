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

public class PMuteCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    String line = MainClass.Line;
    String BANNER;
    public PMuteCMD() {
        super("pmute", "", "mute", "permamute", "permmute");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if (c instanceof ProxiedPlayer) {
            BANNER = c.getName();
        } else {
            BANNER = servername;
        }
        if (c.hasPermission(servername + ".mute.permanently")) {
            if (args.length >= 2) {
                String reason = "";
                for (int i = 1; i != args.length; i++)
                    reason = reason + args[i] + " ";
                reason = reason.replace('&', '§');
                String target = String.valueOf(args[0]);
                ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
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
                    ip = t.getAddress().toString();
                }
                if (targetUUID == null) {
                    c.sendMessage(prefix + "Dieser Spieler existiert nicht.");
                    return;
                }
                String[] ips = ip.split(":");
                String iptrim = ips[0].replace('.', '_');
                String Mute_Banner = BANNER;
                String Mute_Reason = reason;
                String Mute_Expiry = "Permanent";
                try {
                    if (new MuteManager(target, targetUUID.toString()).getMuted() == true) {
                        c.sendMessage(prefix + "Dieser Spieler ist bereits gemuted.");
                    } else {
                        try {
                            int actuallyCount = new HistoryManager(target, targetUUID.toString()).getActuallyCount();
                            int newCount = actuallyCount + 1;
                            new MuteManager(target, targetUUID.toString()).setMuted(iptrim.replace("/", ""), reason, "Permanent", -1, BANNER, true, true);
                            new HistoryManager(target, targetUUID.toString()).setHistory(iptrim.replace("", ""), reason, Mute_Expiry, -1 , Mute_Banner, "nobody", "§c§lMute", false, String.valueOf(newCount));
                            new AutoBanManager().setIpStatusMuted(iptrim.replace("/", ""), true);
                            c.sendMessage(prefix + "Du hast den Spieler §b" + target + " §7erfolgreich gebannt.");
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                new TablistManager().setTablist(all);
                                if (all.hasPermission(servername + ".Ban.notify")) {
                                    all.sendMessage(line);
                                    all.sendMessage(prefix + "§7Art: §c§lMute");
                                    all.sendMessage(prefix + "§7Name: §b" + target);
                                    all.sendMessage(prefix + "§7Von: §b" + BANNER);
                                    all.sendMessage(prefix + "§7Grund: §b" + reason);
                                    all.sendMessage(prefix + "§7Dauer: §bPermanent");
                                    all.sendMessage(line);
                                }
                            }
                            System.out.println(line);
                            System.out.println(prefix + "§7Art: §c§lMute");
                            System.out.println(prefix + "§7Name: §b" + target);
                            System.out.println(prefix + "§7Von: §b" + BANNER);
                            System.out.println(prefix + "§7Grund: §b" + reason);
                            System.out.println(prefix + "§7Dauer: §b" + Mute_Expiry);
                            System.out.println(line);
                            if (!(t == null)) {
                                t.sendMessage(line);
                                t.sendMessage(prefix + "Du wurdest soeben §c§lGEMUTET§7.");
                                t.sendMessage(prefix + "§7Von: §b" + BANNER);
                                t.sendMessage(prefix + "§7Grund: §b" + reason);
                                t.sendMessage(prefix + "§7Dauer: §b" + Mute_Expiry);
                                t.sendMessage(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                c.sendMessage(prefix + "Benutze: §b/Mute §7<§bSpieler§7> <§bGrund§7>");
            }
        } else {
            c.sendMessage(noperm);
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
