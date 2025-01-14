package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FreezeCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public FreezeCommand() {
        getInstance().getCommand("freeze").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target == player) {
                        getUserdata().setBoolean(target, "settings.frozen", !getUserdata().isFrozen(target));
                        if (getUserdata().isFrozen(target)) {
                            player.sendMessage(getMessage().get("commands.freeze.enable", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.freeze.disable", target.getName()));
                    } else if (!target.hasPermission("essentials.command.freeze.exempt")) {
                        getUserdata().setBoolean(target, "settings.frozen", !getUserdata().isFrozen(target));
                        if (getUserdata().isFrozen(target)) {
                            player.sendMessage(getMessage().get("commands.freeze.enable", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.freeze.disable", target.getName()));
                    } else player.sendMessage(getMessage().get("commands.freeze.exempt", target.getName()));
                } else {
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    if (getUserdata().exists(offlinePlayer)) {
                        getUserdata().setBoolean(offlinePlayer, "settings.frozen", !getUserdata().isFrozen(offlinePlayer));
                        if (getUserdata().isFrozen(offlinePlayer)) {
                            player.sendMessage(getMessage().get("commands.freeze.enable", offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("commands.freeze.disable", offlinePlayer.getName()));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    getUserdata().setBoolean(target, "settings.frozen", !getUserdata().isFrozen(target));
                    if (getUserdata().isFrozen(target)) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.freeze.enable", target.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.freeze.disable", target.getName()));
                } else {
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    if (getUserdata().exists(offlinePlayer)) {
                        getUserdata().setBoolean(offlinePlayer, "settings.frozen", !getUserdata().isFrozen(offlinePlayer));
                        if (getUserdata().isFrozen(offlinePlayer)) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.freeze.enable", offlinePlayer.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.freeze.disable", offlinePlayer.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
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
                    if (!getUserdata().isVanished(target)) {
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