package net.tiam.addictzone_proxy.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.AutoBanManager;
import net.tiam.addictzone_proxy.managers.BanManager;
import net.tiam.addictzone_proxy.managers.HistoryManager;
import net.tiam.addictzone_proxy.managers.IPManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class TBanCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    String line = MainClass.Line;
    String BANNER;
    public TBanCMD() {
        super("tban", "", "tempban");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if(!(c.hasPermission(servername + ".Tempban"))){
            c.sendMessage(noperm);
            return;
        }
        if (c instanceof ProxiedPlayer) {
            BANNER = c.getName();
        } else {
            BANNER = servername;
        }
        if(args.length >= 3) {
            String reason = "";
            for (int i = 2; i != args.length; i++)
                reason = reason + args[i] + " ";
            String target = args[0];
            ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
            UUID targetUUID = getUUIDFromName(args[0]);
            String ip = "";
            String banner = BANNER;
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
            int time = Integer.valueOf(args[1].substring(0, args[1].length() - 1));
            long min = 1000 * 60;
            long hour = 1000 * 60 * 60;
            long day = 1000 * 60 * 60 * 24;
            try {
                BanManager banManager = new BanManager(target, targetUUID.toString());
                if (banManager.getBanned() == true) {
                    c.sendMessage(prefix + "Dieser Spieler ist bereits gebannt.");
                    return;
                }
                long longExpiry = args[1].endsWith("m") ? (min * time) : args[1].endsWith("h") ? (hour * time) : args[1].endsWith("d") ? (day * time) : 0;
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                format.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                String expiry = format.format(new Date(System.currentTimeMillis() + longExpiry));
                long banexpiry = System.currentTimeMillis() + longExpiry;
                int actuallyCount = new HistoryManager(target, targetUUID.toString()).getActuallyCount();
                int newCount = actuallyCount + 1;
                String TBanKickMsg = "§9§lAddictZone §8➜ §4§lGEBANNT\n\n§7Von: §b" + BANNER + "\n§7Grund: §b" + reason + "\n§7Dauer: §b" + expiry + "\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §bhttps://AddictZone.net/Forum";
                if (permittedTimePermission(((ProxiedPlayer) c).getUniqueId()) * day < longExpiry) {
                    c.sendMessage(prefix + "Du kannxt maximal §b" + permittedTimePermission(((ProxiedPlayer) c).getUniqueId()) + " §7Tage bannen.");
                    return;
                }
                banManager.setBanned(iptrim, reason, expiry, banexpiry, BANNER, true, false);
                new AutoBanManager().setIPStatusBanned(iptrim, true);
                new HistoryManager(target, targetUUID.toString()).setHistory(iptrim, reason, expiry, banexpiry, banner, "nobody", "§4§lBann", false, String.valueOf(newCount));
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    if (all.hasPermission(servername + ".ban.notify")) {
                        all.sendMessage(line);
                        all.sendMessage(prefix + "§7Art: §4§lBann");
                        all.sendMessage(prefix + "§7Name: §b" + target);
                        all.sendMessage(prefix + "§7Von: §b" + BANNER);
                        all.sendMessage(prefix + "§7Grund: §b" + reason);
                        all.sendMessage(prefix + "§7Dauer: §b" + expiry);
                        all.sendMessage(line);
                    }
                }
                System.out.println(line);
                System.out.println(prefix + "§7Art: §4§lBann");
                System.out.println(prefix + "§7Name: §b" + target);
                System.out.println(prefix + "§7Von: §b" + BANNER);
                System.out.println(prefix + "§7Grund: §b" + reason);
                System.out.println(prefix + "§7Dauer: §b" + expiry);
                System.out.println(line);
                if (!(t == null)) {
                    t.disconnect(TBanKickMsg);
                }
            } catch (IOException e) {
                c.sendMessage(prefix + "Benutze: §b/Tempban §7<§bSpieler§7> <§bDauer§7> <§bGrund§7>");
            }
        } else {
            c.sendMessage(prefix + "Benutze: §b/Tempban §7<§bSpieler§7> <§bDauer§7> <§bGrund§7>");
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
    public static int permittedTimePermission(UUID player) {
        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);
        String servername = MainClass.ServerName;
        for (int i = 1000; i >= 0; i--) {
            if (p.hasPermission(servername + ".Tempban.time." + i)) {
                return i;
            }
        }
        return 0;
    }
}
