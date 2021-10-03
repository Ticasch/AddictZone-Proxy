package net.tiam.addictzone_proxy.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.*;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class BanListCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    String line = MainClass.Line;
    String names = "";
    public BanListCMD() {
        super("banlist");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if (!(c.hasPermission(servername + ".banlist"))) {
            c.sendMessage(noperm);
            return;
        }
        if (args.length == 0) {
            try {
                CommandSender p = ProxyServer.getInstance().getConsole();
                if (!c.getName().equalsIgnoreCase("CONSOLE")) {
                    p = ProxyServer.getInstance().getPlayer(c.getName());
                }
                if (new BanManager(c.getName(), c.getName()).getBannedUsers().size() > 0) {
                    getBanned(c, 1);
                    TextComponent pageSelect = new TextComponent(prefix + "§7Vorherieg Seite §8| ");
                    TextComponent next = new TextComponent("§bNächste Seite");
                    next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Banlist 2"));
                    pageSelect.addExtra(next);
                    if (new BanManager("", "").getBannedUsers().size() > 20) {
                        c.sendMessage(pageSelect);
                    } else {
                        c.sendMessage(prefix + "Vorherige Seite §8| §7Nächste Seite");
                    }
                    c.sendMessage(line);
                } else {
                    c.sendMessage(prefix + "Derzeit sind keine User gebannt.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 1) {
            if (!isInteger(args[0])) {
                c.sendMessage(prefix + "Du musst eine Zahl angeben.");
                return;
            }
            if (Integer.parseInt(args[0]) < 1 || Integer.parseInt(args[0]) > getPages()) {
                c.sendMessage(prefix + "Bitte gebe eine zahl zwischen §b1 §7und §b" + getPages() + " §7an.");
                return;
            }
            try {
                if (new BanManager("", "").getBannedUsers().size() > 0) {
                    getBanned(c, Integer.parseInt(args[0]));
                    TextComponent pageSelect = new TextComponent(prefix);
                    TextComponent before = new TextComponent("§bVorherige Seite");
                    TextComponent next = new TextComponent("§bNächste Seite");
                    before.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Banlist " + (Integer.parseInt(args[0]) - 1)));
                    next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Banlist " + (Integer.parseInt(args[0]) + 1)));
                    if (Integer.parseInt(args[0]) == 1 && getPages() == 1) {
                        pageSelect.addExtra("§7Vorherige Seite §8| §7Nächste Seite");
                    } else if (Integer.parseInt(args[0]) == 1 && getPages() > 1) {
                        pageSelect.addExtra("§7Vorherige Seite §8| ");
                        pageSelect.addExtra(next);
                    } else if (Integer.parseInt(args[0]) > 1) {
                        if (Integer.parseInt(args[0]) < getPages()) {
                            pageSelect.addExtra(before);
                            pageSelect.addExtra(" §8| ");
                            pageSelect.addExtra(next);
                        } else {
                            pageSelect.addExtra(before);
                            pageSelect.addExtra(" §8| §7Nächste Seite");
                        }
                    }
                    c.sendMessage(pageSelect);
                    c.sendMessage(line);
                } else {
                    c.sendMessage(prefix + "Derzeit sind keine Spieler gebannt.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            c.sendMessage(prefix + "Benutze: §b/Banlist");
        }
    }
    public void getBanned(CommandSender c, int arg) {
        try {
            c.sendMessage(line);
            c.sendMessage(prefix + "Insgesammt sind §b" + new BanManager("", "").getBannedUsers().size() + " §7Spieler gebannt.");
            c.sendMessage(prefix + "Bannliste Seite §b" + arg + " §7von §b" + getPages());
            for (String users : new BanManager("", "").getBannedUsers()) {
                String target = new BanManager("", users).getName();
                String targetUUID = users;
                String ip = new BanManager(target, targetUUID.toString()).getIp();
                String[] ips = ip.split(":");
                String iptrim = ips[0].replace('.', '_').replace("/", "");
                if (new BanManager(target, targetUUID.toString()).getBanned() && new BanManager(target, targetUUID.toString()).getExpiryLong() <= System.currentTimeMillis() && new BanManager(target, targetUUID.toString()).getExpiryLong() > 0 && new BanManager(target, targetUUID.toString()).getPermanently() == false) {
                    new BanManager(target, targetUUID.toString()).deleteBan();
                    new AutoBanManager().setIPStatusBanned(iptrim, false);
                    new HistoryManager(target, targetUUID.toString()).settaken(true, servername + "§7 - §cAutomatisch§7", new HistoryManager(target, targetUUID.toString()).getActuallyCountAll());
                }
                names = names + users + ":";
            }
            Msg(c, arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    public void Msg(CommandSender c, int arg) {
        try {
            int max = 0;
            if (new BanManager("", "").getBannedUsers().size() > (arg * 20)) {
                max = (arg * 20);
            } else {
                max = new BanManager("", "").getBannedUsers().size();
            }
            for (int i = ((arg - 1) * 20); i < max; i++) {
                if (i <= new BanManager("", "").getBannedUsers().size()) {
                    TextComponent name = new TextComponent(prefix + "§b" + new BanManager("", new BanManager("", "").getBannedUsers().get(i)).getName() + " §7(von: §b" + new BanManager("", new BanManager("", "").getBannedUsers().get(i)).getBanner() + "§7)" + " §8[§b" + (i+1) + "§8]");
                    name.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Check " + new BanManager("", new BanManager("", "").getBannedUsers().get(i)).getName()));
                    c.sendMessage(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    public int getPages() {
        int page = 0;
        for (int i = 0; i <= 100; i++) {
            try {
                if (new BanManager("", "").getBannedUsers().size() > (20 * i)) {
                    page++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return page;
    }
    public static boolean isInteger(String strNum) {
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException|NullPointerException nfe) {
            return false;
        }
        return true;
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
