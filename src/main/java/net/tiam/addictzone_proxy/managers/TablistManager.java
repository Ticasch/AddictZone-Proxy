package net.tiam.addictzone_proxy.managers;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.w3c.dom.Text;

import java.io.IOException;

public class TablistManager {

    public void setTablist(ProxiedPlayer p) {
        int slots = 0;
        try {
            slots = new SettingsManager().getSlots();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String header = "\n§8» §9§lAddictZone §8«\n§7Spieler online: §b" + ProxyServer.getInstance().getOnlineCount() + "§7/§b" + slots + "\n";
        String footer = "\n§9§lTeamSpeak: §bAddictZone.net\n§9§lDiscord: §b/Discord\n";
        p.setTabHeader((BaseComponent) new TextComponent(header), (BaseComponent) new TextComponent(footer));
    }
}
