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
            if (args.length == 0) {
                if (!getKits().getListed().isEmpty()) {
                    player.sendMessage(getMessage().get("commands.kit.title"));
                    getKits().getListed().forEach(kits -> {
                        if (player.hasPermission("essentials.command.kit." + kits)) {
                            player.sendMessage(getMessage().get("commands.kit.listed", kits));
                        }
                    });
                } else player.sendMessage(getMessage().get("commands.kit.empty"));
                return true;
            } else if (args.length == 1) {
                var kitName = args[0];
                var timer = getKits().getCooldown(kitName);
                if (getKits().isListed(kitName)) {
                    if (player.hasPermission("essentials.command.kit." + kitName)) {
                        if (!getCooldown().has(player, kitName, timer)) {
                            if (getKits().hasPrice(kitName)) {
                                if (getEconomy().has(player, getKits().getPrice(kitName))) {
                                    getMaterials().giveItemStacks(player, getKits().get(kitName));
                                    getEconomy().remove(player, getKits().getPrice(kitName));
                                    getCooldown().add(player, kitName, timer);
                                    player.sendMessage(getMessage().get("commands.kit.receive", kitName));
                                } else player.sendMessage(getMessage().get("commands.kit.non-sufficient-funds", getEconomy().currency() + getEconomy().format(getKits().getPrice(kitName)), kitName));
                            } else {
                                getMaterials().giveItemStacks(player, getKits().get(kitName));
                                getCooldown().add(player, kitName, timer);
                                player.sendMessage(getMessage().get("commands.kit.receive", kitName));
                            }
                        } else player.sendMessage(getMessage().get("commands.kit.cooldown", getCooldown().get(player, kitName, timer)));
                        return true;
                    }
                } else player.sendMessage(getMessage().get("commands.kit.invalid", kitName));
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.kit.other")) {
                    var target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        if (!target.hasPermission("essentials.command.kit.exempt")) {
                            var kitName = args[0];
                            if (getKits().isListed(kitName)) {
                                getMaterials().giveItemStacks(target, getKits().get(kitName));
                                target.sendMessage(getMessage().get("commands.kit.receive", kitName));
                                player.sendMessage(getMessage().get("commands.kit.sender", kitName, target.getName()));
                            } else player.sendMessage(getMessage().get("commands.kit.invalid", kitName));
                        } else player.sendMessage(getMessage().get("commands.kit.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                if (!getKits().getListed().isEmpty()) {
                    consoleCommandSender.sendMessage(getMessage().get("commands.kit.title"));
                    getKits().getListed().forEach(kits -> {
                        consoleCommandSender.sendMessage(getMessage().get("commands.kit.listed", kits));
                    });
                } else consoleCommandSender.sendMessage(getMessage().get("commands.kit.empty"));
                return true;
            } else if (args.length == 2) {
                var target = sender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    var kitName = args[0];
                    if (getKits().isListed(kitName)) {
                        getMaterials().giveItemStacks(target, getKits().get(kitName));
                        target.sendMessage(getMessage().get("commands.kit.receive", kitName));
                        consoleCommandSender.sendMessage(getMessage().get("commands.kit.sender", kitName, target.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.kit.invalid", kitName));
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