package net.tiam.addictzone_proxy.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.ItemSerializer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.HistoryManager;

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
            try {
                int actuallyCount = new HistoryManager(target, targetUUID.toString()).getActuallyCount();
                if (actuallyCount > 0) {
                    c.sendMessage(line);
                    c.sendMessage(prefix + "History von: §b" + target);
                    for (int i = 1; i <= actuallyCount; i++) {
                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        format.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                        String bandate = format.format(new Date(new HistoryManager(target, targetUUID.toString()).getBandate(i)));
                        TextComponent info = new TextComponent(prefix + "§b" + bandate + " §7- " + new HistoryManager(target, targetUUID.toString()).getType(i));
                        info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/History " + target + " " + i));
                        c.sendMessage(info);
                    }
                    c.sendMessage(line);
                } else {
                    c.sendMessage(prefix + "Dieser Spieler hat keine History");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 2) {
            String target = String.valueOf(args[0]);
            UUID targetUUID = getUUIDFromName(target);
            try {
                int actuallyCount = new HistoryManager(target, targetUUID.toString()).getActuallyCount();
                if (Integer.parseInt(args[1]) <= actuallyCount) {
                    if (Integer.parseInt(args[1]) != 0) {
                        int count = Integer.parseInt(args[1]);
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
                        c.sendMessage(prefix + "Bitte gebe eine Zahl über §b0 §7an.");
                    }
                } else {
                    c.sendMessage(prefix + "Dieser Spieler hat nur §b" + actuallyCount + " 7Strafen.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            c.sendMessage(prefix + "Benutze: §b/History §7<§bSpieler§7>");
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
