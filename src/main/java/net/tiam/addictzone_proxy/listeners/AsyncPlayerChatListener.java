package net.tiam.addictzone_proxy.listeners;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsyncPlayerChatListener implements Listener {
    String servername = MainClass.ServerName;
    String prefix = MainClass.Prefix;
    String noperm = MainClass.Prefix + MainClass.NoPerm;
    String line = MainClass.Line;
    @EventHandler
    public void onChat (ChatEvent e) throws IOException {
        ProxiedPlayer p = (ProxiedPlayer) e.getSender();
        String muter = new MuteManager(p.getName(), p.getUniqueId().toString()).getBanner();
        String reason = new MuteManager(p.getName(), p.getUniqueId().toString()).getReason();
        String expiry = new MuteManager(p.getName(), p.getUniqueId().toString()).getExpiry();
        String ip = e.getSender().getAddress().getHostName().toString();
        String[] ips = ip.split(":");
        String iptrim = ips[0].replace('.', '_').replace("/", "");
        if (new MuteManager(p.getName(), p.getUniqueId().toString()).getMuted() == true && !p.hasPermission(servername + ".mute.bypass")) {
            if ((new MuteManager(((ProxiedPlayer) e.getSender()).getName(), ((ProxiedPlayer) e.getSender()).getUniqueId().toString()).getExpiryLong()) <= (System.currentTimeMillis()) && (new MuteManager(((ProxiedPlayer) e.getSender()).getName(), ((ProxiedPlayer) e.getSender()).getUniqueId().toString()).getExpiryLong() > 0) && (new MuteManager(((ProxiedPlayer) e.getSender()).getName(), ((ProxiedPlayer) e.getSender()).getUniqueId().toString()).getPermanently() == false)) {
                new MuteManager(((ProxiedPlayer) e.getSender()).getName(), ((ProxiedPlayer) e.getSender()).getUniqueId().toString()).deleteMute();
                new AutoBanManager().setIpStatusMuted(iptrim, false);
                new HistoryManager(((ProxiedPlayer) e.getSender()).getName(), ((ProxiedPlayer) e.getSender()).getUniqueId().toString()).settaken(true, servername + " §7- §cAutomatisch§7", new HistoryManager(((ProxiedPlayer) e.getSender()).getName(), ((ProxiedPlayer) e.getSender()).getUniqueId().toString()).getActuallyCountAll());
            }
            if (!e.getMessage().startsWith("/")) {
                System.out.println(prefix + "§7[§cMuted§7]§b " + e.getSender() + " §8➜ §7" + e.getMessage());
                e.setCancelled(true);
                p.sendMessage(line);
                p.sendMessage(prefix + "Du bist derzeit §c§lGEMUTET§7.");
                p.sendMessage(prefix + "§7Von: §b" + muter);
                p.sendMessage(prefix + "§7Grund: §b" + reason);
                if (new MuteManager(p.getName(), p.getUniqueId().toString()).getPermanently() == true) {
                    p.sendMessage(prefix + "§7Dauer: §bPermanent");
                } else {
                    p.sendMessage(prefix + "§7Dauer: §b" + expiry);
                }
                p.sendMessage(line);
            } else if (e.getMessage().startsWith("/msg ") || e.getMessage().startsWith("/r ") || e.getMessage().startsWith("@C ")) {
                System.out.println(prefix + "§7[§cMuted§7]§b " + e.getSender() + " §8➜ §7" + e.getMessage());
                e.setCancelled(true);
                p.sendMessage(line);
                p.sendMessage(prefix + "Du bist derzeit §c§lGEMUTET§7.");
                p.sendMessage(prefix + "§7Von: §b" + muter);
                p.sendMessage(prefix + "§7Grund: §b" + reason);
                if (new MuteManager(p.getName(), p.getUniqueId().toString()).getPermanently() == true) {
                    p.sendMessage(prefix + "§7Dauer: §bPermanent");
                } else {
                    p.sendMessage(prefix + "§7Dauer: §b" + expiry);
                }
                p.sendMessage(line);
            }
        }
    }
    @EventHandler
    public void onBlock(ChatEvent e) throws IOException {
        if (e.isCancelled())
            return;
        CommandSender c = (CommandSender) e.getSender();
        CommandSender console = ProxyServer.getInstance().getConsole();
        String message = e.getMessage().replace("!", "i").replace("1", "i").replace(".", "").replace(",", "").replace("#", "").replace("&", "");
        String[] msg = message.split(" ");
        for (int i = 0; i != msg.length; i++) {
            if (!c.hasPermission(servername + ".ChatFilter.Bypass") && new ChatFilterManager().getList().contains(msg[i].toLowerCase())) {
                e.setCancelled(true);
                c.sendMessage(prefix + "Die Wortwahl §b" + msg[i] + " §7ist nicht erlaubt.");
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    if (all.hasPermission(servername + ".ChatFilter.Notify")) {
                        all.sendMessage(prefix + "Der Spieler §b" + c.getName() + " §7hatte folgende");
                        all.sendMessage(prefix + "Wortwahl in seiner Nachricht: §B" + msg[i]);
                    }
                }
                console.sendMessage(prefix + "Der Spieler §b" + c.getName() + " §7hatte folgenden Begriff");
                console.sendMessage(prefix + "in seiner Nachricht: §B" + msg[i]);
            }
        }
    }
}
