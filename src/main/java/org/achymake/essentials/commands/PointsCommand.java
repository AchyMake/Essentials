package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.PointsHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PointsCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private PointsHandler getPointsHandler() {
        return getInstance().getPointsHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public PointsCommand() {
        getInstance().getCommand("points").setExecutor(this);
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.sendMessage(getMessage().get("commands.points.self", getPointsHandler().format(getPointsHandler().get(player))));
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    if (player.hasPermission("essentials.command.points.top")) {
                        player.sendMessage(getMessage().get("commands.points.top.title"));
                        var list = new ArrayList<>(getPointsHandler().getTopPoints());
                        for (int i = 0; i < list.size(); i++) {
                            player.sendMessage(getMessage().get("commands.points.top.listed", String.valueOf(i + 1), list.get(i).getKey().getName(), getPointsHandler().format(list.get(i).getValue())));
                        }
                        return true;
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("check")) {
                    if (player.hasPermission("essentials.commands.points.check")) {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                        if (getUserdata().exists(offlinePlayer)) {
                            player.sendMessage(getMessage().get("commands.points.check", offlinePlayer.getName(), getPointsHandler().format(getPointsHandler().get(offlinePlayer))));
                        } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        return true;
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (player.hasPermission("essentials.commands.points.add")) {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                        var amount = getMessage().getInteger(args[2]);
                        if (getUserdata().exists(offlinePlayer)) {
                            if (getPointsHandler().add(offlinePlayer, amount)) {
                                player.sendMessage(getMessage().get("commands.points.add", getPointsHandler().format(amount), offlinePlayer.getName()));
                            }
                        } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (player.hasPermission("essentials.commands.points.set")) {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                        var amount = getMessage().getInteger(args[2]);
                        if (getUserdata().exists(offlinePlayer)) {
                            if (getPointsHandler().set(offlinePlayer, amount)) {
                                player.sendMessage(getMessage().get("commands.points.set", getPointsHandler().format(amount), offlinePlayer.getName()));
                            }
                        } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (player.hasPermission("essentials.commands.points.remove")) {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                        var amount = getMessage().getInteger(args[2]);
                        if (getUserdata().exists(offlinePlayer)) {
                            if (getPointsHandler().has(offlinePlayer, amount)) {
                                if (getPointsHandler().remove(offlinePlayer, amount)) {
                                    player.sendMessage(getMessage().get("commands.points.remove.success", getPointsHandler().format(amount), offlinePlayer.getName()));
                                }
                            } else player.sendMessage(getMessage().get("commands.points.remove.insufficient-points", getPointsHandler().format(amount), offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("pay")) {
                    if (player.hasPermission("essentials.commands.points.pay")) {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                        var amount = getMessage().getInteger(args[2]);
                        if (getUserdata().exists(offlinePlayer)) {
                            if (getPointsHandler().has(player, amount)) {
                                if (getPointsHandler().add(offlinePlayer, amount)) {
                                    if (getPointsHandler().remove(player, amount)) {
                                        player.sendMessage(getMessage().get("commands.points.pay.success", offlinePlayer.getName(), getPointsHandler().format(amount)));
                                    }
                                }
                            } else player.sendMessage(getMessage().get("commands.points.pay.insufficient-points", getPointsHandler().format(amount)));
                        } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.points.check")) {
                    commands.add("check");
                }
                if (player.hasPermission("essentials.command.points.pay")) {
                    commands.add("pay");
                }
                if (player.hasPermission("essentials.command.points.add")) {
                    commands.add("add");
                }
                if (player.hasPermission("essentials.command.points.remove")) {
                    commands.add("remove");
                }
                if (player.hasPermission("essentials.command.points.set")) {
                    commands.add("set");
                }
                if (player.hasPermission("essentials.command.points.top")) {
                    commands.add("top");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (player.hasPermission("essentials.command.points.add")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!getUserdata().isVanished(target)) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                } else if (args[0].equalsIgnoreCase("check")) {
                    if (player.hasPermission("essentials.command.points.check")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!getUserdata().isVanished(target)) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (player.hasPermission("essentials.command.points.remove")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!getUserdata().isVanished(target)) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (player.hasPermission("essentials.command.points.set")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!getUserdata().isVanished(target)) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                } else if (args[0].equalsIgnoreCase("pay")) {
                    if (player.hasPermission("essentials.command.points.pay")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!getUserdata().isVanished(target)) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                }
            }
        }
        return commands;
    }
}