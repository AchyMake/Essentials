package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomesCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public HomesCommand() {
        getInstance().getCommand("homes").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else {
                if (args.length == 0) {
                    if (!getUserdata(player).getHomes().isEmpty()) {
                        getMessage().send(player, "&6Homes:");
                        for (String listedHomes : getUserdata(player).getHomes()) {
                            getMessage().send(player, "- " + listedHomes);
                        }
                    } else getMessage().send(player, "&cYou haven't set any homes yet");
                    return true;
                } else if (args.length == 3) {
                    var target = args[1];
                    var targetHome = args[2];
                    if (args[0].equalsIgnoreCase("delete")) {
                        if (player.hasPermission("essentials.command.homes.delete")) {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(target);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                if (userdataOffline.getHomes().contains(targetHome)) {
                                    userdataOffline.setString("homes." + targetHome, null);
                                    getMessage().send(player, "&6Deleted&f " + targetHome + "&6 of&f " + target);
                                } else getMessage().send(player, target + "&c doesn't have&f " + targetHome);
                                return true;
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("teleport")) {
                        if (player.hasPermission("essentials.command.homes.teleport")) {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(target);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                if (targetHome.equalsIgnoreCase("bed")) {
                                    var location = offlinePlayer.getBedSpawnLocation();
                                    if (location != null) {
                                        player.teleport(location);
                                        getMessage().send(player, "&6Teleporting&f " + targetHome + "&6 of&f " + target);
                                    } else getMessage().send(player, target + "&c do not have a bed");
                                } else if (userdataOffline.getHomes().contains(targetHome)) {
                                    var location = userdataOffline.getHome(targetHome);
                                    if (location != null) {
                                        location.getChunk().load();
                                        getMessage().send(player, "&6Teleporting&f " + targetHome + "&6 of&f " + target);
                                        player.teleport(location);
                                    }
                                } else getMessage().send(player, target + "&c doesn't have&f " + targetHome);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.homes.delete")) {
                    commands.add("delete");
                }
                if (player.hasPermission("essentials.command.homes.teleport")) {
                    commands.add("teleport");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("teleport")) {
                    if (player.hasPermission("essentials.command.homes.teleport")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!getUserdata(target).isVanished()) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("essentials.command.homes.delete")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!getUserdata(target).isVanished()) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                }
            } else if (args.length == 3) {
                if (player.hasPermission("players.command.homes.teleport")) {
                    var offlinePlayer = sender.getServer().getOfflinePlayer(args[1]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        commands.addAll(userdataOffline.getHomes());
                    }
                }
            }
        }
        return commands;
    }
}