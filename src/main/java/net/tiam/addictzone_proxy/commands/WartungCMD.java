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
import net.tiam.addictzone_proxy.managers.ChatFilterManager;
import net.tiam.addictzone_proxy.managers.SettingsManager;
import net.tiam.addictzone_proxy.managers.UserManager;
import net.tiam.addictzone_proxy.managers.WartungManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

public class WartungCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    String line = MainClass.Line;
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
                c.sendMessage(prefix + "Benutze: §b/Wartung Bypass §7<§bliste§7|§badd§7|§bremove§7> <§bSpieler§7>");
            } else {
                c.sendMessage(prefix + "Benutze: §b/Wartung Status");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("bypass")) {
                if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("liste")) {
                    try {
                        if (new WartungManager().getBypassedUsers().size() > 0) {
                            getBypassedUsers(c, 1);
                            TextComponent pageSelect = new TextComponent(prefix);
                            TextComponent next = new TextComponent("§bNächste Seite");
                            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Wartung Bypass List " + (2)));
                            if (getPages() > 1) {
                                pageSelect.addExtra("§7Vorherige Seite §8| ");
                                pageSelect.addExtra(next);
                            } else {
                                pageSelect.addExtra("§7Vorherige Seite §8| §7Nächste Seite");
                            }
                            c.sendMessage(pageSelect);
                            c.sendMessage(line);
                        } else {
                            c.sendMessage(prefix + "Derzeit sind keine Spieler auf der Wartung-ByPass Liste.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    c.sendMessage(prefix + "Benutze: §b/Wartung Bypass §7<§bliste§7|§badd§7|§bremove§7>");
                }
            } else {
                c.sendMessage(prefix + "Benutze: §b/Wartung Bypass §7<§bliste§7|§badd§7|§bremove§7>");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("Bypass")) {
                if (!c.hasPermission(servername + ".Wartung.Bypass.Edit")) {
                    c.sendMessage(prefix + "Du hast keine Rechte, die Watungs-ByPass Liste zu editieren.");
                    return;
                }
                try {
                    if (args[1].equalsIgnoreCase("add")) {
                        ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[2]);
                        String target = String.valueOf(args[2]);
                        UUID targetUUID = getUUIDFromName(target);
                        if (targetUUID == null) {
                            c.sendMessage(prefix + "Dieser Spieler existiert nicht.");
                            return;
                        }
                        if (new WartungManager().getBypassedUsers().contains(targetUUID.toString())) {
                            c.sendMessage(prefix + "Dieser Speielr ist bereits auf der Wartungs-BaPass Liste.");
                            return;
                        }
                        new WartungManager().getBypassedUsers().add(targetUUID.toString());
                        new WartungManager().setName(targetUUID.toString(), target);
                        c.sendMessage(prefix + "Du hast §b" + target + " §7zur Wartungs-ByPass Liste hinzugefügt.");
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[2]);
                        String target = String.valueOf(args[2]);
                        UUID targetUUID = getUUIDFromName(target);
                        if (targetUUID == null) {
                            c.sendMessage(prefix + "Dieser Spieler existiert nicht.");
                            return;
                        }
                        if (!new WartungManager().getBypassedUsers().contains(targetUUID.toString())) {
                            c.sendMessage(prefix + "Dieser Spieler ist nicht auf der Wartungs-ByPass Liste.");
                            return;
                        }
                        new WartungManager().setName(targetUUID.toString(), null);
                        c.sendMessage(prefix + "Du hast §b" + target + " §7von der Wartungs-ByPass Liste entfernt.");
                    } else if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("liste")) {
                        try {
                            if (new WartungManager().getBypassedUsers().size() > 0) {
                                if (!isInteger(args[2])) {
                                    c.sendMessage(prefix + "Du musst eine Zahl angeben.");
                                    return;
                                }
                                int page = Integer.parseInt(args[2]);
                                if (page < 1 || page > getPages()) {
                                    c.sendMessage(prefix + "Bitte gebe eine zahl zwischen §b1 §7und §b" + getPages() + " §7an.");
                                    return;
                                }
                                getBypassedUsers(c, page);
                                TextComponent pageSelect = new TextComponent(prefix);
                                TextComponent before = new TextComponent("§bVorherige Seite");
                                TextComponent next = new TextComponent("§bNächste Seite");
                                before.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Wartung Bypass List " + (page - 1)));
                                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Wartung Bypass List " + (page + 1)));
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
                                c.sendMessage(prefix + "Derzeit sind keine Spieler auf der Wartung-ByPass Liste.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        c.sendMessage(prefix + "Benutze: §b/Wartung Bypass §7<§bliste§7|§badd§7|§bremove§7>");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                c.sendMessage(prefix + "Benutze: §b/Wartung §7<§bStatus§7|§bBypass§7>");
            }
        } else {
            c.sendMessage(prefix + "Benutze: §b/Wartung §7<§bStatus§7|§bBypass§7>");
        }
    }
    public void getBypassedUsers(CommandSender c, int arg) {
        try {
            c.sendMessage(line);
            c.sendMessage(prefix + "Insgesammt sind §b" + new WartungManager().getBypassedUsers().size() + " §7Spieler auf der Wartung-ByPass Liste.");
            c.sendMessage(prefix + "Wartung-ByPsss Liste Seite §b" + arg + " §7von §b" + getPages());
            Msg(c, arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    public void Msg(CommandSender c, int arg) {
        try {
            int max = 0;
            if (new WartungManager().getBypassedUsers().size() > (arg * 20)) {
                max = (arg * 20);
            } else {
                max = new WartungManager().getBypassedUsers().size();
            }
            for (int i = ((arg - 1) * 20); i < max; i++) {
                if (i <= new ChatFilterManager().getList().size()) {
                    TextComponent name = new TextComponent(prefix + "§b" + new WartungManager().getName(new WartungManager().getBypassedUsers().get(i)) + " §8[§b" + (i + 1) + "§8]");
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
                if (new WartungManager().getBypassedUsers().size() > (20 * i)) {
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
