package net.tiam.addictzone_proxy;

import com.google.common.collect.Iterables;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.tiam.addictzone_proxy.commands.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;

public final class MainClass extends Plugin implements Listener {

    public static String ServerName = "AddictZone";
    public static String Prefix = "§9§lAddictZone §8➜ §7";
    public static String NoPerm = "§7Dazu hast du keine Rechte!";
    public static String Line = "§8§m--------------------------------------------------";
    public boolean wartung;
    public Configuration config;
    private static MainClass instance;
    private Collection<? extends ProxiedPlayer> onlineplayer;
    String kickmessage = "§9§lAddictZone §7- §6§lWartungen\n\n§7Unser Netzwerk befindet sich in §6Wartungsarbeiten§7.\n§7Wir werden bald wieder Erreichbar sein.\n\n§7TeamSpeak: §bAddictZone.net\n§7Forum: §bhttps://AddictZone.net/Forum";

    public static MainClass get() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        registerCommands();
        wartung = config.getBoolean("Status");
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        loadDefaultFiles();
    }

    @Override
    public void onDisable() {
    }

    public void registerCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new LobbyCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new AdminChatCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PcCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PopCMD());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new WartungCMD());
        //ProxyServer.getInstance().getPluginManager().registerCommand(this, new TeamChatCMD());
    }
    public void loadDefaultFiles() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File configfile = new File(getDataFolder(), "config.yml");
        if (!configfile.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, configfile.toPath());
            } catch (IOException e) {
                System.out.println("Fehler");
            }
        }
        try {
            loadFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadFiles() throws IOException {
        config = ConfigurationProvider.getProvider(YamlConfiguration.class)
                .load(new File(getDataFolder(), "config.yml"));
    }
    public void saveFiles() throws IOException {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config,
                new File(getDataFolder(), "config.yml"));
    }

    public void toggleWartung(boolean ein) throws IOException {
        if (ein) {
            ProxyServer.getInstance().broadcast(
                    (Prefix + "§7Wartungen sind nun aktiviert.")
                            .replaceAll("&", "§"));
            onlineplayer = ProxyServer.getInstance().getPlayers();
            for (int i = 0; i < onlineplayer.size(); i++) {
                ProxiedPlayer player = Iterables.get(onlineplayer, i);
                if (!player.hasPermission("addictzone.wartung.bypass")) {
                    player.disconnect(kickmessage);
                }
            }
            wartung = true;
        } else {
            wartung = false;
            ProxyServer.getInstance().broadcast((Prefix + "§7Wartungen sind nun deaktivert.").replaceAll("&", "§"));
        }
        config.set("BungeeWartung.Status", wartung);
        saveFiles();
    }
    public void onLogin2(LoginEvent e) {
        if (wartung) {
            e.setCancelled(true);
            e.setCancelReason(kickmessage);
        }
    }
}
