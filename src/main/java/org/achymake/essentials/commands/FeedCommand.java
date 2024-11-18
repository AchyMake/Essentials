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
            if (args.length == 0) {
                int timer = getInstance().getConfig().getInt("commands.cooldown.feed");
                if (timer > 0) {
                    if (!getCooldown().has(player, "feed", timer)) {
                        player.setFoodLevel(20);
                        player.sendMessage(getMessage().get("commands.feed.success"));
                        getCooldown().add(player, "feed", timer);
                    } else getMessage().sendActionBar(player, getMessage().get("commands.feed.cooldown", getCooldown().get(player, "feed", timer)));
                } else {
                    player.setFoodLevel(20);
                    player.sendMessage(getMessage().get("commands.feed.success"));
                }
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.feed.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (!target.hasPermission("essentials.command.feed.exempt")) {
                            target.setFoodLevel(20);
                            target.sendMessage(getMessage().get("commands.feed.success"));
                            player.sendMessage(getMessage().get("commands.feed.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.feed.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    target.sendMessage(getMessage().get("commands.feed.success"));
                    consoleCommandSender.sendMessage(getMessage().get("commands.feed.sender", target.getName()));
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