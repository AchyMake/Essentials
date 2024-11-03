package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Jail;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JailCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Jail getJail() {
        return getInstance().getJail();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public JailCommand() {
        getInstance().getCommand("jail").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var target = player.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    var jail = getJail().getLocation();
                    if (jail != null) {
                        var userdataTarget = getUserdata(target);
                        if (target == player) {
                            if (userdataTarget.isJailed()) {
                                var recent = userdataTarget.getLocation("recent");
                                if (recent != null) {
                                    if (!recent.getChunk().isLoaded()) {
                                        recent.getChunk().load();
                                    }
                                    target.teleport(recent);
                                }
                                userdataTarget.setBoolean("settings.jailed", !userdataTarget.isJailed());
                                player.sendMessage(getMessage().get("commands.jail.disable", target.getName()));
                            } else {
                                if (!jail.getChunk().isLoaded()) {
                                    jail.getChunk().load();
                                }
                                target.teleport(jail);
                                player.sendMessage(getMessage().get("commands.jail.enable", target.getName()));
                            }
                        } else if (!target.hasPermission("essentials.command.jail.exempt")) {
                            if (userdataTarget.isJailed()) {
                                var recent = userdataTarget.getLocation("recent");
                                if (recent != null) {
                                    if (!recent.getChunk().isLoaded()) {
                                        recent.getChunk().load();
                                    }
                                    target.teleport(recent);
                                }
                                userdataTarget.setBoolean("settings.jailed", !userdataTarget.isJailed());
                                player.sendMessage(getMessage().get("commands.jail.disable", target.getName()));
                            } else {
                                if (!jail.getChunk().isLoaded()) {
                                    jail.getChunk().load();
                                }
                                target.teleport(jail);
                                player.sendMessage(getMessage().get("commands.jail.enable", target.getName()));
                            }
                        } else player.sendMessage(getMessage().get("commands.jail.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("commands.jail.invalid"));
                } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    var jail = getJail().getLocation();
                    if (jail != null) {
                        var userdataTarget = getUserdata(target);
                        if (userdataTarget.isJailed()) {
                            var recent = userdataTarget.getLocation("recent");
                            if (recent != null) {
                                if (!recent.getChunk().isLoaded()) {
                                    recent.getChunk().load();
                                }
                                target.teleport(recent);
                            }
                            userdataTarget.setBoolean("settings.jailed", !userdataTarget.isJailed());
                            consoleCommandSender.sendMessage(getMessage().get("commands.jail.disable", target.getName()));
                        } else {
                            if (!jail.getChunk().isLoaded()) {
                                jail.getChunk().load();
                            }
                            target.teleport(jail);
                            consoleCommandSender.sendMessage(getMessage().get("commands.jail.enable", target.getName()));
                        }
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.jail.invalid"));
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata(target).isVanished()) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            }
        }
        return commands;
    }
}