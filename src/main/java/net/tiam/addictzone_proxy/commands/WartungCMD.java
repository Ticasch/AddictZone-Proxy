package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;

import java.io.IOException;

public class WartungCMD extends Command {
    public WartungCMD() {
        super("wartung");
    }
    String prefix = MainClass.Prefix;

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean wartung = MainClass.get().config.getBoolean("Status");
        if (sender.hasPermission("addictzone.command.wartung")) {
            if (wartung == true) {
                try {
                    MainClass.get().toggleWartung(false);
                } catch (IOException e) {
                    sender.sendMessage(prefix + "§4Config Fehler");
                }
            } else {
                try {
                    MainClass.get().toggleWartung(true);
                } catch (IOException e) {
                    sender.sendMessage(prefix + "§4Config Fehler");
                }
            }
        }
    }
}
