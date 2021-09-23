package net.tiam.addictzone_proxy.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.MuteManager;

import java.io.IOException;

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
        if (new MuteManager(p.getName(), p.getUniqueId().toString()).getMuted() == true && !p.hasPermission(servername + ".mute.bypass")) {
            if (!e.getMessage().startsWith("/")) {
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

}
