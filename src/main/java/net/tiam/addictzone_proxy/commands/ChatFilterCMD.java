package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.AutoBanManager;
import net.tiam.addictzone_proxy.managers.BanManager;
import net.tiam.addictzone_proxy.managers.ChatFilterManager;
import net.tiam.addictzone_proxy.managers.HistoryManager;

import java.io.IOException;

public class ChatFilterCMD extends Command {
    String prefix = MainClass.Prefix;
    String noperm = MainClass.NoPerm;
    String servername = MainClass.ServerName;
    String line = MainClass.Line;
    public ChatFilterCMD() {
        super("ChatFilter");
    }
    @Override
    public void execute(CommandSender c, String[] args) {
        if (!c.hasPermission(servername + ".ChatFilter.Use")) {
            c.sendMessage(noperm);
            return;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("List") || args[0].equalsIgnoreCase("Liste")) {
                try {
                    if (new ChatFilterManager().getList().size() > 0) {
                        getWords(c, 1);
                        TextComponent pageSelect = new TextComponent(prefix + "§7Vorherieg Seite §8| ");
                        TextComponent next = new TextComponent("§bNächste Seite");
                        next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ChatFilter List 2"));
                        pageSelect.addExtra(next);
                        if (new ChatFilterManager().getList().size() > 20) {
                            c.sendMessage(pageSelect);
                        } else {
                            c.sendMessage(prefix + "Vorherige Seite §8| §7Nächste Seite");
                        }
                        c.sendMessage(line);
                    } else {
                        c.sendMessage(prefix + "Derzeit sind keine Wörter gesperrt.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
                if (c.hasPermission(servername + ".ChatFilter.Edit")) {
                    c.sendMessage(prefix + "Benutze: §b/ChatFilter §7<§badd§7|§bremove§7> <§bWort§7>");
                } else {
                    c.sendMessage(noperm);
                }
            } else {
                if (c.hasPermission(servername + ".ChatFilter.Edit")) {
                    c.sendMessage(prefix + "Benutze: §b/ChatFilter §7<§bListe§7|§badd§7|§bremove§7>");
                } else {
                    c.sendMessage(prefix + "Benutze: §b/ChatFilter List");
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("Liste") || args[0].equalsIgnoreCase("List")) {
                try {
                    if (new ChatFilterManager().getList().size() > 0) {
                        if (!isInteger(args[1])) {
                            c.sendMessage(prefix + "Du musst eine Zahl angeben.");
                            return;
                        }
                        int page = Integer.parseInt(args[1]);
                        if (page < 1 || page > getPages()) {
                            c.sendMessage(prefix + "Bitte gebe eine zahl zwischen §b1 §7und §b" + getPages() + " §7an.");
                            return;
                        }
                        getWords(c, page);
                        TextComponent pageSelect = new TextComponent(prefix);
                        TextComponent before = new TextComponent("§bVorherige Seite");
                        TextComponent next = new TextComponent("§bNächste Seite");
                        before.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ChatFilter List " + (page - 1)));
                        next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ChatFilter List " + (page + 1)));
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
                        c.sendMessage(prefix + "Derzeit sind keine Wörter gesperrt.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (args[0].equalsIgnoreCase("add")) {
                if (!c.hasPermission(servername + ".ChatFilter.Edit")) {
                    c.sendMessage(noperm);
                    return;
                }
                String word = String.valueOf(args[1]);
                try {
                if (new ChatFilterManager().getList().contains(word)) {
                    c.sendMessage(prefix + "Dieses Wort ist bereits auf der ChatFilter-Liste.");
                    return;
                }
                    c.sendMessage(prefix + "Du hast das Wort §b" + word + " §7zur ChatFilter-Liste hinzugefügt.");
                    new ChatFilterManager().getList().add(word);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (!c.hasPermission(servername + ".ChatFilter.Edit")) {
                    c.sendMessage(noperm);
                    return;
                }
                String word = String.valueOf(args[1]);
                try {
                    if (!new ChatFilterManager().getList().contains(word)) {
                        c.sendMessage(prefix + "Dieses Wort ist nicht auf der ChatFilter-Liste.");
                        return;
                    }
                    c.sendMessage(prefix + "Du hast das Wort §b" + word + " §7von der ChatFilter-Liste entfernt.");
                    new ChatFilterManager().getList().remove(word);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (c.hasPermission(servername + ".ChatFilter.Edit")) {
                    c.sendMessage(prefix + "Benutze: §b/ChatFilter §7<§bListe§7|§badd§7|§bremove§7>");
                } else {
                    c.sendMessage(prefix + "Benutze: §b/ChatFilter List");
                }
            }
        } else {
            if (c.hasPermission(servername + ".ChatFilter.Edit")) {
                c.sendMessage(prefix + "Benutze: §b/ChatFilter §7<§bListe§7|§badd§7|§bremove§7>");
            } else {
                c.sendMessage(prefix + "Benutze: §b/ChatFilter List");
            }
        }
    }
    public void getWords(CommandSender c, int arg) {
        try {
            c.sendMessage(line);
            c.sendMessage(prefix + "Insgesammt sind §b" + new ChatFilterManager().getList().size() + " §7Wörter gesperrt.");
            c.sendMessage(prefix + "ChatFilter-Liste Seite §b" + arg + " §7von §b" + getPages());
            Msg(c, arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    public void Msg(CommandSender c, int arg) {
        try {
            int max = 0;
            if (new ChatFilterManager().getList().size() > (arg * 20)) {
                max = (arg * 20);
            } else {
                max = new ChatFilterManager().getList().size();
            }
            for (int i = ((arg - 1) * 20); i < max; i++) {
                if (i <= new ChatFilterManager().getList().size()) {
                    TextComponent name = new TextComponent(prefix + "§b" + new ChatFilterManager().getList().get(i) + " §8[§b" + (i + 1) + "§8]");
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
                if (new ChatFilterManager().getList().size() > (20 * i)) {
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
}
