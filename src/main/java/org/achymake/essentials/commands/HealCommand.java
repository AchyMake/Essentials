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

public class HealCommand implements CommandExecutor, TabCompleter {
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
    public HealCommand() {
        getInstance().getCommand("heal").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                int timer = getInstance().getConfig().getInt("commands.cooldown.heal");
                if (!getCooldown().has(player, "heal", timer)) {
                    player.setFoodLevel(20);
                    player.setHealth(player.getMaxHealth());
                    getMessage().sendActionBar(player, "&6Your health has been satisfied");
                    getCooldown().add(player, "heal", timer);
                } else getMessage().sendActionBar(player, "&cYou have to wait&f " + getCooldown().get(player, "heal", timer) + "&c seconds");
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.heal.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            target.setFoodLevel(20);
                            target.setHealth(target.getMaxHealth());
                            getMessage().sendActionBar(target, "&6Your health has been satisfied by&f " + player.getName());
                            getMessage().send(player, "&6You satisfied&f " + target.getName() + "&6's health");
                        } else if (!target.hasPermission("essentials.command.heal.exempt")) {
                            target.setFoodLevel(20);
                            target.setHealth(target.getMaxHealth());
                            getMessage().sendActionBar(target, "&6Your health has been satisfied by&f " + player.getName());
                            getMessage().send(player, "&6You satisfied&f " + target.getName() + "&6's health");
                        } else getMessage().send(player, command.getPermissionMessage());
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    target.setHealth(target.getMaxHealth());
                    getMessage().sendActionBar(target, "&6Your health has been satisfied");
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
                if (player.hasPermission("essentials.command.heal.other")) {
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