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

public class PBanCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = prefix + MainClass.NoPerm;
    String servername = MainClass.ServerName;
    String line = MainClass.Line;
    String BANNER;
    String Ban_Expiry = "";
    String Ban_Banner = "";
    String Ban_Reason = "";
    public PBanCMD() {
        super("pban", "", "ban", "permaban", "permban");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if (c instanceof ProxiedPlayer) {
            BANNER = c.getName();
        } else {
            BANNER = servername;
        }
        if (c.hasPermission(servername + ".ban.permanently")) {
            if (args.length >= 2) {
                String reason = "";
                for (int i = 1; i != args.length; i++)
                    reason = reason + args[i] + " ";
                reason = reason.replace('&', '§');
                String target = String.valueOf(args[0]);
                ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
                UUID targetUUID = getUUIDFromName(target);
                if (targetUUID == null) {
                    c.sendMessage(prefix + "Dieser Spieler ist nicht registriert.");
                    return;
                }
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
                    ip = t.getAddress().getAddress().getHostAddress();
                }
                if (targetUUID == null) {
                    c.sendMessage(prefix + "Dieser Spieler existiert nicht.");
                    return;
                }
                String[] ips = ip.split(":");
                String iptrim = ips[0].replace('.', '_');
                String Ban_Banner = BANNER;
                String Ban_Reason = reason;
                String Ban_Expiry = "Permanent";
                String PBanKickMsg = "§9§lAddictZone §8➜ §4§lGEBANNT\n\n§7Von: §b" + Ban_Banner + "\n§7Grund: §b" + Ban_Reason + "\n§7Dauer: §b" + Ban_Expiry + "\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §bhttps://AddictZone.net/Forum\n§7Discord-Verify-Server: §bVerify.AddictZone.eu";
                try {
                    if (new BanManager(target, targetUUID.toString()).getBanned() == true) {
                        c.sendMessage(prefix + "Dieser Spieler ist bereits gebannt.");
                    } else {
                        try {
                            int actuallyCount = new HistoryManager(target, targetUUID.toString()).getActuallyCount();
                            int newCount = actuallyCount + 1;
                            new BanManager(target, targetUUID.toString()).setBanned(iptrim.replace("/", ""), reason, Ban_Expiry, -1, BANNER, true, true);
                            new HistoryManager(target, targetUUID.toString()).setHistory(iptrim.replace("/", ""), reason, Ban_Expiry, -1, BANNER, "nobody", "§4§lBann", false, String.valueOf(newCount));
                            new AutoBanManager().setIPStatusBanned(iptrim.replace("/", ""), true);
                            c.sendMessage(prefix + "Du hast den Spieler §b" + target + " §7erfolgreich gebannt.");
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                new TablistManager().setTablist(all);
                                if (all.hasPermission(servername + ".Ban.notify")) {
                                    all.sendMessage(line);
                                    all.sendMessage(prefix + "§7Art: §4§lBann");
                                    all.sendMessage(prefix + "§7Name: §b" + target);
                                    all.sendMessage(prefix + "§7Von: §b" + BANNER);
                                    all.sendMessage(prefix + "§7Grund: §b" + reason);
                                    all.sendMessage(prefix + "§7Dauer: §bPermanent");
                                    all.sendMessage(line);
                                }
                            }
                            System.out.println(line);
                            System.out.println(prefix + "§7Art: §4§lBann");
                            System.out.println(prefix + "§7Name: §b" + target);
                            System.out.println(prefix + "§7Von: §b" + BANNER);
                            System.out.println(prefix + "§7Grund: §b" + reason);
                            System.out.println(prefix + "§7Dauer: §b" + Ban_Expiry);
                            System.out.println(line);
                            if (!(t == null)) {
                                t.disconnect(PBanKickMsg);
                            }
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                UUID uuid = getUUIDFromName(all.getName());
                                String ip1 = all.getAddress().getAddress().getHostAddress();
                                String[] ips1 = ip1.split(":");
                                String iptrim1 = ips1[0].replace(".", "_").replace("/", "");
                                if (new AutoBanManager().getIPStatusBanned(iptrim1) && (all.toString() != target)) {
                                    ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "Ban " + all.getName() + " Bannumgehung §7(§cAccount Liste§7)");
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                c.sendMessage(prefix + "Benutze: §b/Ban §7<§bSpieler§7> <§bGrund§7>");
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
