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
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getJail().getLocation() != null) {
                        var userdataTarget = getUserdata(target);
                        if (target == player) {
                            toggleJail(target);
                            if (userdataTarget.isJailed()) {
                                player.sendMessage(getMessage().get("commands.jail.disable", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.jail.enable", target.getName()));
                        } else if (!target.hasPermission("essentials.command.jail.exempt")) {
                            toggleJail(target);
                            if (userdataTarget.isJailed()) {
                                player.sendMessage(getMessage().get("commands.jail.disable", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.jail.enable", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.jail.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("commands.jail.invalid"));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getJail().getLocation() != null) {
                        toggleJail(target);
                        var userdataTarget = getUserdata(target);
                        if (userdataTarget.isJailed()) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.jail.disable", target.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.jail.enable", target.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.jail.invalid"));
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
    private void toggleJail(Player target) {
        var userdataTarget = getUserdata(target);
        userdataTarget.setBoolean("settings.jailed", !userdataTarget.isJailed());
        if (userdataTarget.isJailed()) {
            userdataTarget.setLocation(target.getLocation(), "jail");
            var location = getJail().getLocation();
            if (location != null) {
                location.getChunk().load();
                target.teleport(location);
            }
        } else {
            var location = userdataTarget.getLocation("jail");
            if (location != null) {
                location.getChunk().load();
                target.teleport(location);
            }
            userdataTarget.setString("locations.jail", null);
        }
    }
}