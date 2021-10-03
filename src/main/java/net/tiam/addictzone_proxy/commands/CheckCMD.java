package net.tiam.addictzone_proxy.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class CheckCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    String line = MainClass.Line;
    public CheckCMD() {
        super("check");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if (!c.hasPermission(servername + ".check")) {
            c.sendMessage(noperm);
            return;
        }
        if (args.length == 1) {
            String target = args[0];
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
            String iptrim = ips[0].replace('.', '_').replace("/", "");
            try {
                if (new MuteManager(target, targetUUID.toString()).getMuted() && new MuteManager(target, targetUUID.toString()).getExpiryLong() <= System.currentTimeMillis() && new MuteManager(target, targetUUID.toString()).getExpiryLong() > 0 && new MuteManager(target, targetUUID.toString()).getPermanently() == false) {
                        new MuteManager(target, targetUUID.toString()).deleteMute();
                    new AutoBanManager().setIpStatusMuted(iptrim, false);
                    new HistoryManager(target, targetUUID.toString()).settaken(true, servername + "§7 - §cAutomatisch§7", new HistoryManager(target, targetUUID.toString()).getActuallyCountAll());
                }
                if (new BanManager(target, targetUUID.toString()).getBanned() && new BanManager(target, targetUUID.toString()).getExpiryLong() <= System.currentTimeMillis() && new BanManager(target, targetUUID.toString()).getExpiryLong() > 0 && new BanManager(target, targetUUID.toString()).getPermanently() == false) {
                    new BanManager(target, targetUUID.toString()).deleteBan();
                    new AutoBanManager().setIPStatusBanned(iptrim, false);
                    new HistoryManager(target, targetUUID.toString()).settaken(true, servername + "§7 - §cAutomatisch§7", new HistoryManager(target, targetUUID.toString()).getActuallyCountAll());
                }
                if (new BanManager(target, targetUUID.toString()).getBanned() == false && new MuteManager(target, targetUUID.toString()).getMuted() == false) {
                    c.sendMessage(prefix + "Dieser Spieler ist derzeit nicht bestraft.");
                    return;
                }
                c.sendMessage(line);
                c.sendMessage(prefix + "Strafstatus von: §b" + target);
                if (new MuteManager(target, targetUUID.toString()).getMuted() == true) {
                    c.sendMessage(prefix + "Art: §c§lMute");
                    c.sendMessage(prefix + "Name: §b" + target);
                    c.sendMessage(prefix + "Von: §b" + new MuteManager(target, targetUUID.toString()).getBanner());
                    c.sendMessage(prefix + "Grund: §b" + new MuteManager(target, targetUUID.toString()).getReason());
                    c.sendMessage(prefix + "Dauer: §b" + new MuteManager(target, targetUUID.toString()).getExpiry());
                }
                if (new MuteManager(target, targetUUID.toString()).getMuted() == true && new BanManager(target, targetUUID.toString()).getBanned() == true)
                    c.sendMessage(prefix);
                if (new BanManager(target, targetUUID.toString()).getBanned() == true) {
                    c.sendMessage(prefix + "Art: §4§lBann");
                    c.sendMessage(prefix + "Name: §b" + target);
                    c.sendMessage(prefix + "Von: §b" + new BanManager(target, targetUUID.toString()).getBanner());
                    c.sendMessage(prefix + "Grund: §b" + new BanManager(target, targetUUID.toString()).getReason());
                    c.sendMessage(prefix + "Dauer: §b" + new BanManager(target, targetUUID.toString()).getExpiry());
                }
                c.sendMessage(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            c.sendMessage(prefix + "Benutze: §b/Check §7<§bSpieler§7>");
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
