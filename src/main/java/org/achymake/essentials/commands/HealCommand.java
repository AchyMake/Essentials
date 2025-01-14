package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.CooldownHandler;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HealCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
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
            if (args.length == 0) {
                int timer = getInstance().getConfig().getInt("commands.cooldown.heal");
                if (!getCooldown().has(player, "heal", timer)) {
                    player.setFoodLevel(20);
                    player.setHealth(player.getMaxHealth());
                    getCooldown().add(player, "heal", timer);
                    player.sendMessage(getMessage().get("commands.heal.success"));
                } else getMessage().sendActionBar(player, getMessage().get("commands.heal.cooldown", getCooldown().get(player, "heal", timer)));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.heal.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            target.setFoodLevel(20);
                            target.setHealth(target.getMaxHealth());
                            target.sendMessage(getMessage().get("commands.heal.success"));
                            player.sendMessage(getMessage().get("commands.heal.sender", target.getName()));
                        } else if (!target.hasPermission("essentials.command.heal.exempt")) {
                            target.setFoodLevel(20);
                            target.setHealth(target.getMaxHealth());
                            target.sendMessage(getMessage().get("commands.heal.success"));
                            player.sendMessage(getMessage().get("commands.heal.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.heal.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    target.setHealth(target.getMaxHealth());
                    target.sendMessage(getMessage().get("commands.heal.success"));
                    consoleCommandSender.sendMessage(getMessage().get("commands.heal.sender", target.getName()));
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
                if (player.hasPermission("essentials.command.heal.other")) {
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