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
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else {
                if (args.length == 1) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (getJail().getLocation() != null) {
                            if (target == player) {
                                if (getJail().getLocation() != null) {
                                    if (getUserdata(target).isJailed()) {
                                        var location = getUserdata(target).getLocation("jail");
                                        if (location != null) {
                                            location.getChunk().load();
                                            target.teleport(location);
                                        }
                                        getUserdata(target).setBoolean("settings.jailed", false);
                                        getMessage().send(target, "&cYou got free by&f " + sender.getName());
                                        getMessage().send(player, "&6You freed&f " + target.getName());
                                        getUserdata(target).setString("locations.jail", null);
                                    } else {
                                        getUserdata(target).setLocation(target.getLocation(), "jail");
                                        var location = getJail().getLocation();
                                        if (location != null) {
                                            location.getChunk().load();
                                            target.teleport(location);
                                        }
                                        getUserdata(target).setBoolean("settings.jailed", true);
                                        getMessage().send(target, "&cYou got jailed by&f " + sender.getName());
                                        getMessage().send(player, "&6You jailed&f " + target.getName());
                                    }
                                } else getMessage().send(player, "&cJail has not been set");
                            } else if (!target.hasPermission("players.command.jail.exempt")) {
                                if (getUserdata(target).isJailed()) {
                                    var location = getUserdata(target).getLocation("jail");
                                    if (location != null) {
                                        location.getChunk().load();
                                        target.teleport(location);
                                    }
                                    getUserdata(target).setBoolean("settings.jailed", false);
                                    getMessage().send(target, "&cYou got free by&f " + sender.getName());
                                    getMessage().send(player, "&6You freed&f " + target.getName());
                                    getUserdata(target).setString("locations.jail", null);
                                } else {
                                    getUserdata(target).setLocation(target.getLocation(), "jail");
                                    var location = getJail().getLocation();
                                    if (location != null) {
                                        location.getChunk().load();
                                        target.teleport(location);
                                    }
                                    getUserdata(target).setBoolean("settings.jailed", true);
                                    getMessage().send(target, "&cYou got jailed by&f " + sender.getName());
                                    getMessage().send(player, "&6You jailed&f " + target.getName());
                                }
                            } else getMessage().send(player, command.getPermissionMessage());
                        } else getMessage().send(player, "&cJail has not been set");
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getJail().getLocation() != null) {
                        if (getUserdata(target).isJailed()) {
                            var location = getUserdata(target).getLocation("jail");
                            if (location != null) {
                                location.getChunk().load();
                                target.teleport(location);
                            }
                            getUserdata(target).setBoolean("settings.jailed", false);
                            getMessage().send(target, "&cYou got free");
                            getUserdata(target).setString("locations.jail", null);
                        } else {
                            getUserdata(target).setLocation(target.getLocation(), "jail");
                            var location = getJail().getLocation();
                            if (location != null) {
                                location.getChunk().load();
                                target.teleport(location);
                            }
                            getUserdata(target).setBoolean("settings.jailed", true);
                            getMessage().send(target, "&cYou got jailed by&f " + consoleCommandSender.getName());
                        }
                    } else consoleCommandSender.sendMessage("Jail has not been set");
                    return true;
                }
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