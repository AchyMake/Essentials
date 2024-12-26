package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScoreboardHandler;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ScoreboardHandler getScoreboard() {
        return getInstance().getScoreboardHandler();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public BoardCommand() {
        getInstance().getCommand("board").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getScoreboard().hasBoard(player)) {
                    getScoreboard().disable(player);
                    player.sendMessage(getMessage().get("commands.board.self", getMessage().get("disable")));
                } else {
                    getScoreboard().apply(player);
                    player.sendMessage(getMessage().get("commands.board.self", getMessage().get("enable")));
                }
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.spawn.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            if (getScoreboard().hasBoard(target)) {
                                getScoreboard().disable(target);
                                player.sendMessage(getMessage().get("commands.board.other", target.getName(), getMessage().get("disable")));
                            } else {
                                getScoreboard().apply(target);
                                player.sendMessage(getMessage().get("commands.board.other", target.getName(), getMessage().get("enable")));
                            }
                        } else if (!target.hasPermission("essentials.command.board.exempt")) {
                            if (getScoreboard().hasBoard(target)) {
                                getScoreboard().disable(target);
                                player.sendMessage(getMessage().get("commands.board.other", target.getName(), getMessage().get("disable")));
                            } else {
                                getScoreboard().apply(target);
                                player.sendMessage(getMessage().get("commands.board.other", target.getName(), getMessage().get("enable")));
                            }
                        } else player.sendMessage(getMessage().get("commands.board.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (getScoreboard().hasBoard(target)) {
                        getScoreboard().disable(target);
                        consoleCommandSender.sendMessage(getMessage().get("commands.board.other", target.getName(), getMessage().get("disable")));
                    } else {
                        getScoreboard().apply(target);
                        consoleCommandSender.sendMessage(getMessage().get("commands.board.other", target.getName(), getMessage().get("enable")));
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
                if (player.hasPermission("essentials.command.board.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata().isVanished(target)) {
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