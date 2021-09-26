package net.tiam.addictzone_proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.tiam.addictzone_proxy.MainClass;


public class KickCMD extends Command {
    public KickCMD() {
        super("Kick");
    }
    public void execute(CommandSender sender, String[] args) {
        String prefix = MainClass.Prefix;
        String noperm = MainClass.NoPerm;
        String servername = MainClass.ServerName;
        String line = MainClass.Line;
        String KICKER;
        CommandSender c = (CommandSender) sender;
        if (c instanceof ProxiedPlayer) {
            KICKER = c.getName();
        } else {
            KICKER = servername;
        }
                if (args.length == 1) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    if (target == c) {
                        c.sendMessage(prefix + "Du kannst dich nicht selbst kicken.");
                    } else if (target == null) {
                        c.sendMessage(prefix + "Dieser Spieler ist nicht online.");
                    } else {
                        String message = prefix + "§c§lGEKICKT" + System.lineSeparator() + System.lineSeparator() + "§7Von: §b" + KICKER + System.lineSeparator() + "§7Grund: §b-";
                        if (c.hasPermission(servername + ".Kick")) {
                            if (target.hasPermission(servername + ".Kick.ignore")) {
                                if (c.hasPermission(servername + ".Kick.ignore.bypass")) {
                                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                        if (all.hasPermission(servername + ".Kick.Notify")) {
                                            all.sendMessage(line + System.lineSeparator() + prefix + "§c§lKick" + System.lineSeparator() + prefix + "Name: §b" + target.getName() + System.lineSeparator() + prefix + "Von: §b" + KICKER + System.lineSeparator() + prefix + "Grund: §b-" + System.lineSeparator() + line);
                                        }
                                    }
                                    target.disconnect(message);
                                    c.sendMessage(prefix + "Der Spieler §b" + target.getName() + " §7wurde  gekickt.");
                                    c.sendMessage(prefix + "Grund: §b-");
                                } else {
                                    c.sendMessage(prefix + "Du kannst diesen Spieler nicht kicken.");
                                }
                            } else {
                                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                    if (all.hasPermission(servername + ".Kick.Notify")) {
                                        all.sendMessage(line + System.lineSeparator() + prefix + "§c§lKick" + System.lineSeparator() + prefix + "Name: §b" + target.getName() + System.lineSeparator() + prefix + "Von: §b" + KICKER + System.lineSeparator() + prefix + "Grund: §b-" + System.lineSeparator() + line);
                                    }
                                }
                                target.disconnect(message);
                                c.sendMessage(prefix + "Der Spieler §b" + target.getName() + " §7wurde gekickt.");
                                c.sendMessage(prefix + "Grund: §b-");
                            }
                        } else {
                            c.sendMessage(noperm);
                        }
                    }
                } else if (args.length >= 2) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    if (target == c) {
                        c.sendMessage(prefix + "Du kannst dich nicht selbst kicken.");
                    } else if (target == null) {
                        c.sendMessage(prefix + "Dieser Spieler ist nicht online.");
                    } else {
                        String reason = "";
                        for (int i = 1; i != args.length; i++)
                            reason = reason + args[i] + " ";
                        reason = reason.replace('&', '§');
                        String message = prefix + "§c§lGEKICKT" + System.lineSeparator() + System.lineSeparator() + "§7Von: §b" + KICKER + System.lineSeparator() + "§7Grund: §b" + reason;
                        if (c.hasPermission(servername + ".Kick")) {
                            if (target.hasPermission(servername + ".Kick.ignore")) {
                                if (c.hasPermission(servername + ".Kick.ignore.bypass")) {
                                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                        if (all.hasPermission(servername + ".Kick.Notify")) {
                                            all.sendMessage(line + System.lineSeparator() + prefix + "§c§lKick" + System.lineSeparator() + prefix + "Name: §b" + target.getName() + System.lineSeparator() + prefix + "Von: §b" + KICKER + System.lineSeparator() + prefix + "Grund: §b" + reason + System.lineSeparator() + line);
                                        }
                                    }
                                    target.disconnect(message);
                                    c.sendMessage(prefix + "Der Spieler §b" + target.getName() + " §7wurde gekickt.");
                                    c.sendMessage(prefix + "Grund: §b" + reason);
                                } else {
                                    c.sendMessage(prefix + "Du kannst diesen Spieler nicht kicken.");
                                }
                            } else {
                                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                    if (all.hasPermission(servername + ".Kick.Notify")) {
                                        all.sendMessage(line + System.lineSeparator() + prefix + "§c§lKick" + System.lineSeparator() + prefix + "Name: §b" + target.getName() + System.lineSeparator() + prefix + "Von: §b" + KICKER + System.lineSeparator() + prefix + "Grund: §b" + reason + System.lineSeparator() + line);
                                    }
                                }
                                target.disconnect(message);
                                c.sendMessage(prefix + "Der Spieler §b" + target.getName() + " §7wurde gekickt.");
                                c.sendMessage(prefix + "Grund: §b" + reason);
                            }
                        } else {
                            c.sendMessage(noperm);
                        }
                    }

            } else {
                    c.sendMessage(prefix + "Benutze: §b/Kick §7<§bSpieler§7> <[§cOPTIONAL§7]§bGrund§7>");
                }

    }
}

