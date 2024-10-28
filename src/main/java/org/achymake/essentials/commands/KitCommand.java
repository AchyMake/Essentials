package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Kits;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.CooldownHandler;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private CooldownHandler getCooldown() {
        return getInstance().getCooldownHandler();
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private Kits getKits() {
        return getInstance().getKits();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public KitCommand() {
        getInstance().getCommand("kit").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                if (!getKits().getListed().isEmpty()) {
                    getMessage().send(player, "&6Kits:");
                    getKits().getListed().forEach(kits -> {
                        if (player.hasPermission("essentials.command.kit." + kits)) {
                            getMessage().send(player, "- " + kits);
                        }
                    });
                } else getMessage().send(player, "&cKits are currently empty");
                return true;
            } else if (args.length == 1) {
                var kitName = args[0].toLowerCase();
                var timer = getKits().getCooldown(kitName);
                if (getKits().getListed().contains(kitName)) {
                    if (player.hasPermission("essentials.command.kit." + kitName)) {
                        if (!getCooldown().has(player, kitName, timer)) {
                            if (getKits().hasPrice(kitName)) {
                                if (getEconomy().has(player, getKits().getPrice(kitName))) {
                                    getMaterials().giveItems(player, getKits().getKit(kitName));
                                    getEconomy().remove(player, getKits().getPrice(kitName));
                                    getCooldown().add(player, kitName, timer);
                                    getMessage().send(player, "&6You received&f " + kitName);
                                } else getMessage().send(player, "&cYou do not have&a " + getEconomy().currency() + getEconomy().format(getKits().getPrice(kitName)) + "&c for&f " + kitName + "&c kit");
                            } else {
                                getMaterials().giveItems(player, getKits().getKit(kitName));
                                getCooldown().add(player, kitName, timer);
                                getMessage().send(player, "&6You received&f " + kitName);
                            }
                        } else getMessage().sendActionBar(player, "&cYou have to wait&f " + getCooldown().get(player, kitName, timer) + "&c seconds");
                        return true;
                    }
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.kit.other")) {
                    var target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        if (!target.hasPermission("essentials.command.kit.exempt")) {
                            var kitName = args[0].toLowerCase();
                            if (getKits().getListed().contains(kitName)) {
                                getMaterials().giveItems(target, getKits().getKit(kitName));
                                getMessage().send(target, "&6You received&f " + kitName + "&6 kit");
                                getMessage().send(player, "&6You gave&f " + kitName + "&6 kit to&f " + target.getName());
                            }
                        } else getMessage().send(player, "&cYou are not allowed to send kit to&f " + target.getName());
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                consoleCommandSender.sendMessage("Kits:");
                getKits().getListed().forEach(kits -> consoleCommandSender.sendMessage("- " + kits));
                return true;
            } else if (args.length == 2) {
                var target = sender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    var kitName = args[0].toLowerCase();
                    if (getKits().getListed().contains(kitName)) {
                        getMaterials().giveItems(target, getKits().getKit(kitName));
                        getMessage().send(target, "&6You received&f " + kitName + "&6 kit");
                        consoleCommandSender.sendMessage("You gave " + kitName + " kit to " + target.getName());
                        return true;
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
                getKits().getListed().forEach(kits -> {
                    if (player.hasPermission("essentials.command.kit." + kits)) {
                        if (kits.startsWith(args[0])) {
                            commands.add(kits);
                        }
                    }
                });
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.kit.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata(target).isVanished()) {
                            if (target.getName().startsWith(args[1])) {
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