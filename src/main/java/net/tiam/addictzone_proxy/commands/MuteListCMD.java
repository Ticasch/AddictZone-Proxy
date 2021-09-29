package net.tiam.addictzone_proxy.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.AutoBanManager;
import net.tiam.addictzone_proxy.managers.BanManager;
import net.tiam.addictzone_proxy.managers.HistoryManager;
import net.tiam.addictzone_proxy.managers.MuteManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class MuteListCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    String line = MainClass.Line;
    public MuteListCMD() {
        super("mutelist");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if (!(c.hasPermission(servername + ".mutelist"))) {
            c.sendMessage(noperm);
            return;
        }
        if (args.length == 0) {
            try {
                ProxiedPlayer p = ((ProxiedPlayer) c);
                if (new MuteManager(p.getName(), p.getUniqueId().toString()).getMutedUsers().size() > 0) {
                    c.sendMessage(line);
                    c.sendMessage(prefix + "Derzeit sind folgene Spieler gemutet:");
                    getMuted(c.toString());
                    c.sendMessage(line);
                } else {
                    c.sendMessage(prefix + "Derzeit sind keine User gemutet.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            c.sendMessage(prefix + "Benutze: §b/Mutelist");
        }
    }
    public void getMuted(String c) {
        try {
            for (String users : new MuteManager(c, c).getMutedUsers()) {
                String target = new MuteManager(c, users).getName();
                String targetUUID = users;
                String ip = new MuteManager(target, targetUUID.toString()).getIp();
                String[] ips = ip.split(":");
                String iptrim = ips[0].replace('.', '_').replace("/", "");
                if (new MuteManager(target, targetUUID.toString()).getMuted() && new MuteManager(target, targetUUID.toString()).getExpiryLong() <= System.currentTimeMillis() && new MuteManager(target, targetUUID.toString()).getExpiryLong() > 0 && new MuteManager(target, targetUUID.toString()).getPermanently() == false) {
                    new MuteManager(target, targetUUID.toString()).deleteMute();
                    new AutoBanManager().setIpStatusMuted(iptrim, false);
                    new HistoryManager(target, targetUUID.toString()).settaken(true, servername + "§7 - §cAutomatisch§7", new HistoryManager(target, targetUUID.toString()).getActuallyCount());
                }
                if (new MuteManager(c, users).getMuted() == false) {
                    return;
                }
                TextComponent user = new TextComponent(prefix + "§b" + new MuteManager(c, users).getName() + " §7(von: §b" + new MuteManager(c, users).getBanner() + "§7)");
                user.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Check " + new MuteManager(c, users).getName()));
                CommandSender p = ProxyServer.getInstance().getConsole();
                if (!c.equalsIgnoreCase("CONSOLE")) {
                    p = ProxyServer.getInstance().getPlayer(c);
                }
                p.sendMessage(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
