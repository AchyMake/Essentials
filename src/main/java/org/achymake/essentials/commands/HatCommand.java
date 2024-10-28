package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HatCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public HatCommand() {
        getInstance().getCommand("hat").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else {
                if (args.length == 0) {
                    var heldItem = player.getInventory().getItemInMainHand();
                    if (!getMaterials().isAir(heldItem)) {
                        var helmet = player.getInventory().getHelmet();
                        if (helmet == null) {
                            getMessage().send(player, "&6You are now wearing&f " + getMessage().toTitleCase(heldItem.getType().toString()));
                            player.getInventory().setHelmet(getMaterials().getItem(heldItem.getType().toString(), 1));
                            heldItem.setAmount(heldItem.getAmount() - 1);
                        } else getMessage().send(player, "&cYou are already wearing&f " + getMessage().toTitleCase(helmet.getType().toString()));
                    } else getMessage().send(player, "&cYou have to hold an item");
                    return true;
                } else if (args.length == 1) {
                    if (player.hasPermission("essentials.command.hat.other")) {
                        var target = player.getServer().getPlayerExact(args[0]);
                        if (target != null) {
                            var heldItem = player.getInventory().getItemInMainHand();
                            if (!getMaterials().isAir(heldItem)) {
                                if (target == player) {
                                    var helmet = target.getInventory().getHelmet();
                                    if (helmet == null) {
                                        var name = getMessage().toTitleCase(heldItem.getType().toString());
                                        getMessage().send(player, target.getName() + "&6 is now wearing&f " + name);
                                        target.getInventory().setHelmet(getMaterials().getItem(heldItem.getType().toString(), 1));
                                        heldItem.setAmount(heldItem.getAmount() - 1);
                                    } else getMessage().send(player, target.getName() + "&c is already wearing&f " + getMessage().toTitleCase(helmet.getType().toString()));
                                } else if (!target.hasPermission("essentials.command.hat.exempt")) {
                                    var helmet = target.getInventory().getHelmet();
                                    if (helmet == null) {
                                        var name = getMessage().toTitleCase(heldItem.getType().toString());
                                        getMessage().send(player, target.getName() + "&6 is now wearing&f " + name);
                                        target.getInventory().setHelmet(getMaterials().getItem(heldItem.getType().toString(), 1));
                                        heldItem.setAmount(heldItem.getAmount() - 1);
                                    } else getMessage().send(player, target.getName() + "&c is already wearing&f " + getMessage().toTitleCase(helmet.getType().toString()));
                                } else getMessage().send(player, "&cYou are not allowed to change helmet for&f " + target.getName());
                            } else getMessage().send(player, "&cYou have to hold an item");
                            return true;
                        }
                    }
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
                if (player.hasPermission("essentials.command.hat.other")) {
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