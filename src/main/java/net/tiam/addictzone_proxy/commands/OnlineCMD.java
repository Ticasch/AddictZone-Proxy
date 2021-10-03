package net.tiam.addictzone_proxy.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.AutoBanManager;
import net.tiam.addictzone_proxy.managers.BanManager;
import net.tiam.addictzone_proxy.managers.HistoryManager;
import net.tiam.addictzone_proxy.managers.UserManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class OnlineCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    String line = MainClass.Line;
    String usage = prefix + "Benutze: §b/Online §7<§bList§7|§bInfo§7> <[§cOPTIONAL§7]§bSeite§7>";
    int globalJoins = 0;
    public OnlineCMD() {
        super("online");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if (!c.hasPermission(servername + ".online")) {
            c.sendMessage(noperm);
            return;
        }
        try {
            globalJoins = new UserManager("", "").getUsers().size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (args.length == 0) {
            if (!c.hasPermission(servername + ".Online.Count") && !c.hasPermission(servername + ".Online.*")) {
                c.sendMessage(prefix + "Du hast keine Rechte, zu sehen wieviele Spieler registriert sind.");
                return;
            }
            String countString = NumberFormat(globalJoins);
            c.sendMessage(prefix + "Es sind insgesammt §b" + countString + " §7Spieler registriert.");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("liste") || args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("Spieler")) {
                if (!c.hasPermission(servername + ".Online.list") && !c.hasPermission(servername + ".online.*")) {
                    c.sendMessage(prefix + "Du hast keine Rechte, dir die Liste aller registrierten Spieler anzusehen.");
                    return;
                }
                CommandSender p = ProxyServer.getInstance().getConsole();
                if (!c.getName().equalsIgnoreCase("CONSOLE")) {
                    p = ProxyServer.getInstance().getPlayer(c.getName());
                }
                if (globalJoins > 0) {
                    getList(c, 1);
                    TextComponent pageSelect = new TextComponent(prefix + "§7Vorherieg Seite §8| ");
                    TextComponent next = new TextComponent("§bNächste Seite");
                    next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Online list 2"));
                    pageSelect.addExtra(next);
                    if (globalJoins > 20) {
                        c.sendMessage(pageSelect);
                    } else {
                        c.sendMessage(prefix + "Vorherige Seite §8| §7Nächste Seite");
                    }
                    c.sendMessage(line);
                } else {
                    c.sendMessage(prefix + "Derzeit sind keine Spieler registriert.");
                }
            } else {
                c.sendMessage(usage);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("liste") || args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("Spieler")) {
                if (!c.hasPermission(servername + ".Online.list") && !c.hasPermission(servername + ".online.*")) {
                    c.sendMessage(prefix + "Du hast keine Rechte, dir die Liste aller registrierten Spieler anzusehen.");
                    return;
                }
                if (!isInteger(args[1])) {
                    c.sendMessage(prefix + "Du musst eine zahl angeben.");
                    return;
                }
                int page = Integer.parseInt(args[1]);
                CommandSender p = ProxyServer.getInstance().getConsole();
                if (!c.getName().equalsIgnoreCase("CONSOLE")) {
                    p = ProxyServer.getInstance().getPlayer(c.getName());
                }
                if (globalJoins > 0) {
                    if (page < 1 || page > getPages()) {
                        c.sendMessage(prefix + "Bitte gebe eine Zahl zwischen §b1 §7und §b" + getPages() + " §7an.");
                        return;
                    }
                    getList(c, page);
                    TextComponent pageSelect = new TextComponent(prefix);
                    TextComponent before = new TextComponent("§bVorherige Seite");
                    TextComponent next = new TextComponent("§bNächste Seite");
                    before.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Online list " + (page - 1)));
                    next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Online list " + (page + 1)));
                    if (page == 1 && getPages() == 1) {
                        pageSelect.addExtra("§7Vorherige Seite §8| §7Nächste Seite");
                    } else if (page == 1 && getPages() > 1) {
                        pageSelect.addExtra("§7Vorherige Seite §8| ");
                        pageSelect.addExtra(next);
                    } else if (page > 1) {
                        if (page < getPages()) {
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
                    c.sendMessage(prefix + "Es sind keine Spieler registriert.");
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                if (!c.hasPermission(servername + ".Online.Info") && !c.hasPermission(servername + ".Online.*")) {
                    c.sendMessage(prefix + "Du hast keine Rechte, dir die Informationen, über Spieler anzusehen.");
                    return;
                }
                try {
                    String target = String.valueOf(args[1]);
                    UUID targetUUID = getUUIDFromName(target);
                    if (targetUUID == null) {
                        c.sendMessage(prefix + "Dieser Spieler existiert nicht.");
                        return;
                    }
                    if (!new UserManager(target, targetUUID.toString()).getUsers().contains(targetUUID.toString())) {
                        c.sendMessage(prefix + "Dieser Spieler ist auf dem Netzwerk nicht registriert.");
                        return;
                    }
                    long firstJoinLong = new UserManager(target, targetUUID.toString()).getFirstJoin();
                    long lastJoinLong = new UserManager(target, targetUUID.toString()).getLastJoin();
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    format.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                    String firstJoin = format.format(new Date(firstJoinLong));
                    String lastJoin = format.format(new Date(lastJoinLong));
                    c.sendMessage(line);
                    c.sendMessage(prefix + "Online-Informationen von §b" + new UserManager(target, targetUUID.toString()).getName());
                    c.sendMessage(prefix + "Joins: §b" + new UserManager(target, targetUUID.toString()).getJoins());
                    c.sendMessage(prefix + "First-Join: §b" + firstJoin);
                    c.sendMessage(prefix + "Last-Join: §B" + lastJoin);
                    c.sendMessage(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                c.sendMessage(usage);
            }
        }
    }
    public void getList(CommandSender c, int page) {
        try {
            globalJoins = new UserManager("", "").getUsers().size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            c.sendMessage(line);
            c.sendMessage(prefix + "Insgesammt sind §b" + new BanManager("", "").getBannedUsers().size() + " §7Spieler registriert.");
            c.sendMessage(prefix + "Userliste Seite §b" + page + " §7von §b" + getPages());
            Msg(c, page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    public void Msg(CommandSender c, int page) {
        try {
            int max = 0;
            if (globalJoins > (page * 20)) {
                max = (page * 20);
            } else {
                max = globalJoins;
            }
            for (int i = ((page - 1) * 20); i < max; i++) {
                if (i <= globalJoins) {
                    TextComponent name = new TextComponent(prefix + "§b" + new UserManager("", new UserManager("", "").getUsers().get(i)).getName());
                    name.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Online info " + new UserManager("", new UserManager("", "").getUsers().get(i)).getName()));
                    c.sendMessage(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    public int getPages() {
        try {
            globalJoins = new UserManager("", "").getUsers().size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int page = 0;
        for (int i = 0; i <= 100; i++) {
            if (globalJoins > (20 * i)) {
                page++;
            }
        }
        return page;
    }
    public String NumberFormat(int count) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
        String format = decimalFormat.format(count);
        return format.replace(".", "_").replace(",", ";").replace("_", ",").replace(";", ".");
    }
    public static boolean isInteger(String strNum) {
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
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
