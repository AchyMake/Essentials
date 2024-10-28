package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.CooldownHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FeedCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private CooldownHandler getCooldown() {
        return getInstance().getCooldownHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public FeedCommand() {
        getInstance().getCommand("feed").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            int timer = getInstance().getConfig().getInt("commands.cooldown.feed");
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                if (timer > 0) {
                    if (getCooldown().has(player, "feed", timer)) {
                        var timeLeft = getCooldown().get(player, "feed", timer);
                        getMessage().sendActionBar(player, "&cYou have to wait&f " + timeLeft + "&c seconds");
                    } else {
                        player.setFoodLevel(20);
                        getMessage().sendActionBar(player, "&6Your starvation has been satisfied");
                        getCooldown().add(player, "feed", timer);
                    }
                } else {
                    player.setFoodLevel(20);
                    getMessage().sendActionBar(player, "&6Your starvation has been satisfied");
                }
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("players.command.feed.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target.hasPermission("players.command.feed.exempt")) {
                            getMessage().send(player, command.getPermissionMessage());
                        } else {
                            target.setFoodLevel(20);
                            getMessage().sendActionBar(target, "&6Your starvation has been satisfied");
                            getMessage().send(player, "&6You satisfied&f " + target.getName() + "&6's starvation");
                        }
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    getMessage().sendActionBar(target, "&6Your starvation has been satisfied");
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.feed.other")) {
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