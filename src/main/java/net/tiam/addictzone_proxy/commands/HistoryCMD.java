package net.tiam.addictzone_proxy.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.ItemSerializer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class HistoryCMD extends Command {
    String prefix = MainClass.Prefix;
    String line = MainClass.Line;
    String noperm = prefix + MainClass.NoPerm;
    String servername = MainClass.ServerName;

    public HistoryCMD(){
        super("history");
    }

    @Override
    public void execute(CommandSender c, String[] args) {
        if(!(c.hasPermission(servername + ".History"))){
            c.sendMessage(noperm);
            return;
        }
        if (args.length == 1) {
            String target = String.valueOf(args[0]);
            UUID targetUUID = getUUIDFromName(target);
            if (targetUUID == null) {
                c.sendMessage(prefix + "Dieser Spieler ist nicht registriert.");
                return;
            }
            try {
                int actuallyCount = new HistoryManager(target, targetUUID.toString()).getActuallyCount();
                if (actuallyCount > 0) {
                    c.sendMessage(line);
                    c.sendMessage(prefix + "History von §b" + target + " §7(§b" + actuallyCount + " §7Einträge)");
                    c.sendMessage(prefix + "Seite §b1 §7von §b" + getPages(target, targetUUID.toString()));
                    Msg(c, 1, target, targetUUID.toString());
                    TextComponent pageSelect = new TextComponent(prefix + "§7Vorherieg Seite §8| ");
                    TextComponent next = new TextComponent("§bNächste Seite");
                    next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/History " + target + " page 2"));
                    pageSelect.addExtra(next);
                    if (actuallyCount > 20) {
                        c.sendMessage(pageSelect);
                    } else {
                        c.sendMessage(prefix + "Vorherige Seite §8| §7Nächste Seite");
                    }
                    c.sendMessage(line);
                } else {
                    c.sendMessage(prefix + "Dieser Spieler hat keine History");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("specific")) {
                String target = String.valueOf(args[0]);
                UUID targetUUID = getUUIDFromName(target);
                if (targetUUID == null) {
                    c.sendMessage(prefix + "Dieser Spieler ist nicht registriert.");
                    return;
                }
                ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
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
                        new HistoryManager(target, targetUUID.toString()).settaken(true, servername + "§7 - §cAutomatisch§7", new HistoryManager(target, targetUUID.toString()).getActuallyCount());
                    }
                    if (new BanManager(target, targetUUID.toString()).getBanned() && new BanManager(target, targetUUID.toString()).getExpiryLong() <= System.currentTimeMillis() && new BanManager(target, targetUUID.toString()).getExpiryLong() > 0 && new BanManager(target, targetUUID.toString()).getPermanently() == false) {
                        new BanManager(target, targetUUID.toString()).deleteBan();
                        new AutoBanManager().setIPStatusBanned(iptrim, false);
                        new HistoryManager(target, targetUUID.toString()).settaken(true, servername + "§7 - §cAutomatisch§7", new HistoryManager(target, targetUUID.toString()).getActuallyCount());
                    }
                    try {
                        int actuallyCount = new HistoryManager(target, targetUUID.toString()).getActuallyCount();
                        if (!isInteger(args[2])) {
                            c.sendMessage(prefix + "Du musst eine Zahl angeben.");
                            return;
                        }
                        int count = Integer.parseInt(args[2]);
                        if (count <= actuallyCount) {
                            if (count > 0) {
                                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                format.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                                String bandate = format.format(new Date(new HistoryManager(target, targetUUID.toString()).getBandate(count)));
                                c.sendMessage(line);
                                c.sendMessage(prefix + "History (§b" + count + "§7) von: §b" + target);
                                c.sendMessage(prefix + "Art: " + new HistoryManager(target, targetUUID.toString()).getType(count));
                                c.sendMessage(prefix + "Von: §b" + new HistoryManager(target, targetUUID.toString()).getBanner(count));
                                c.sendMessage(prefix + "Grund: §b" + new HistoryManager(target, targetUUID.toString()).getReason(count));
                                c.sendMessage(prefix + "Dauer: §b" + new HistoryManager(target, targetUUID.toString()).getExpiry(count));
                                if (new HistoryManager(target, targetUUID.toString()).getTaken(count) == false) {
                                    c.sendMessage(prefix + "Aufgehoben: §bNein");
                                } else {
                                    c.sendMessage(prefix + "Aufgehoben: §bJa §7(von: §b" + new HistoryManager(target, targetUUID.toString()).getTaker(count) + "§7)");
                                }
                                c.sendMessage(prefix + "Datum: §b" + bandate);
                                c.sendMessage(line);
                            } else {
                                c.sendMessage(prefix + "Bitte gebe eine Zahl zwischen §b1 §7und §b" + actuallyCount + " §7an.");
                            }
                        } else {
                            c.sendMessage(prefix + "Bitte gebe eine Zahl zwischen §b1 §7und §b" + actuallyCount + " §7an.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (args[1].equalsIgnoreCase("page")) {
                if (!isInteger(args[2])) {
                    c.sendMessage(prefix + "Du musst eine Zahl angeben.");
                    return;
                }
                int arg = Integer.parseInt(args[2]);
                String target = String.valueOf(args[0]);
                UUID targetUUID = getUUIDFromName(target);
                if (arg < 1 || arg > getPages(target, targetUUID.toString())) {
                    c.sendMessage(prefix + "Bitte gebe eine zahl zwischen §b1 §7und §b" + getPages(target, targetUUID.toString()) + " §7an.");
                    return;
                }
                if (targetUUID == null) {
                    c.sendMessage(prefix + "Dieser Spieler ist nicht registriert.");
                    return;
                }
                try {
                    int actuallyCount = new HistoryManager(target, targetUUID.toString()).getActuallyCount();
                    if (actuallyCount > 0) {
                        c.sendMessage(line);
                        c.sendMessage(prefix + "History von §b" + target + " §7(§b" + actuallyCount + " §7Einträge)");
                        c.sendMessage(prefix + "Seite §b" + arg + " §7von §b" + getPages(target, targetUUID.toString()));
                        Msg(c, arg, target, targetUUID.toString());
                        TextComponent pageSelect = new TextComponent(prefix);
                        TextComponent before = new TextComponent("§bVorherige Seite");
                        TextComponent next = new TextComponent("§bNächste Seite");
                        before.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/History " + target + " page " + (Integer.parseInt(args[2]) - 1)));
                        next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/History " + target + " page " + (Integer.parseInt(args[2]) + 1)));
                        if (Integer.parseInt(args[2]) == 1 && getPages(target, targetUUID.toString()) == 1) {
                            pageSelect.addExtra("§7Vorherige Seite §8| §7Nächste Seite");
                        } else if (Integer.parseInt(args[2]) == 1 && getPages(target, targetUUID.toString()) > 1) {
                            pageSelect.addExtra("§7Vorherige Seite §8| ");
                            pageSelect.addExtra(next);
                        } else if (Integer.parseInt(args[2]) > 1) {
                            if (Integer.parseInt(args[2]) < getPages(target, targetUUID.toString())) {
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
                        c.sendMessage(prefix + "Dieser Spieler hat keine History");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                c.sendMessage(prefix + "Benutze: §b/History §7<§bSpieler§7>");
            }
        } else {
            c.sendMessage(prefix + "Benutze: §b/History §7<§bSpieler§7>");
        }
    }
    public void Msg(CommandSender c, int arg, String target, String targetUUID) {
        try {
            int actuallyCount = new HistoryManager(target, targetUUID).getActuallyCount();
            int max = 0;
            if (actuallyCount > (arg * 20)) {
                max = (arg * 20);
            } else {
                max = actuallyCount;
            }
            for (int i = (((arg - 1) * 20) + 1); i <= max; i++) {
                if (i <= actuallyCount) {
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    format.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                    String bandate = format.format(new Date(new HistoryManager(target, targetUUID).getBandate(i)));
                    TextComponent info = new TextComponent(prefix + "§b" + bandate + " §7- " + new HistoryManager(target, targetUUID).getType(i) + " §8[§b" + i + "§8]");
                    info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/History " + target + " Specific " + i));
                    c.sendMessage(info);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    public int getPages(String target, String targetUUID) {
        int page = 0;
        try {
            int actuallyCount = new HistoryManager(target, targetUUID).getActuallyCount();
            for (int i = 0; i <= 100; i++) {
                if (actuallyCount > (20 * i)) {
                    page++;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return page;
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
