package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BackCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public BackCommand() {
        getInstance().getCommand("back").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                var userdata = getUserdata(player);
                var recent = userdata.getLocation("recent");
                var delay = getInstance().getConfig().getInt("teleport.delay");
                if (player.hasPermission("essentials.command.back.death")) {
                    var death = userdata.getLocation("death");
                    if (death != null) {
                        var worldName = death.getWorld().getName().toLowerCase();
                        if (player.hasPermission("essentials.command.back.world." + worldName)) {
                            userdata.teleport(death, "death", delay);
                        }
                    } else if (recent != null) {
                        var worldName = recent.getWorld().getName().toLowerCase();
                        if (player.hasPermission("essentials.command.back.world." + worldName)) {
                            userdata.teleport(recent, "recent", delay);
                        }
                    }
                } else if (recent != null) {
                    var worldName = recent.getWorld().getName().toLowerCase();
                    if (player.hasPermission("essentials.command.back.world." + worldName)) {
                        userdata.teleport(recent, "recent", delay);
                    }
                }
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.back.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            var userdataTarget = getUserdata(target);
                            var recent = userdataTarget.getLocation("recent");
                            if (target.hasPermission("essentials.command.back.death")) {
                                var death = userdataTarget.getLocation("death");
                                if (death != null) {
                                    userdataTarget.teleport(death, "death", 0);
                                } else if (recent != null) {
                                    userdataTarget.teleport(recent, "recent", 0);
                                }
                            } else if (recent != null) {
                                userdataTarget.teleport(recent, "recent", 0);
                            }
                        } else if (!target.hasPermission("essentials.command.back.exempt")) {
                            var userdataTarget = getUserdata(target);
                            var recent = userdataTarget.getLocation("recent");
                            if (target.hasPermission("essentials.command.back.death")) {
                                var death = userdataTarget.getLocation("death");
                                if (death != null) {
                                    userdataTarget.teleport(death, "death", 0);
                                } else if (recent != null) {
                                    userdataTarget.teleport(recent, "recent", 0);
                                }
                            } else if (recent != null) {
                                userdataTarget.teleport(recent, "recent", 0);
                            }
                        } else player.sendMessage(getMessage().get("commands.back.exempt"), target.getName());
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var userdataTarget = getUserdata(target);
                    var recent = userdataTarget.getLocation("recent");
                    if (target.hasPermission("essentials.command.back.death")) {
                        var death = userdataTarget.getLocation("death");
                        if (death != null) {
                            userdataTarget.teleport(death, "death", getInstance().getConfig().getInt("teleport.delay"));
                        } else if (recent != null) {
                            userdataTarget.teleport(recent, "recent", getInstance().getConfig().getInt("teleport.delay"));
                        }
                    } else if (recent != null) {
                        userdataTarget.teleport(recent, "recent", getInstance().getConfig().getInt("teleport.delay"));
                    }
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.back.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata(target).isVanished()) {
                            if (target.getName().startsWith(args[0])) {
                                commands.add(target.getName());
                            }
                        }
                    });
                }
            }
        }
        return commands;
    }
}