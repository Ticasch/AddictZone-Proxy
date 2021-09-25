package net.tiam.addictzone_proxy.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;
import net.tiam.addictzone_proxy.managers.YouTubeManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class YouTuberCMD extends Command {

    public  YouTuberCMD() {
        super("youtuber", "", "yt");
    }
    String prefix = MainClass.Prefix;
    String noPerm = prefix + MainClass.NoPerm;
    String line = MainClass.Line;

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(args.length != 1) {
            sender.sendMessage(prefix + "Benutze §b/youtuber §7<§bSpieler§7>");
        }else{
            String uuid = getUUIDFromName(args[0]).toString();
            try {
                YouTubeManager youTubeManager = new YouTubeManager(uuid);
                if(!(youTubeManager.getYouTuber())) {
                    sender.sendMessage(new TextComponent(prefix + "Dieser Spieler ist kein YouTuber."));
                }else{
                    sender.sendMessage(line);
                    sender.sendMessage(prefix + "§7Gelange hier zu dem YouTube-Kanal von §b"+args[0]+"§7:");
                    sender.sendMessage(prefix);
                    sender.sendMessage(prefix + "§b"+youTubeManager.getLink());
                    sender.sendMessage(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
