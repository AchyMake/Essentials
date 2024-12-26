package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public GiveCommand() {
        getInstance().getCommand("give").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 2) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var amount = 1;
                    var itemStack = getMaterials().getItemStack(args[1], amount);
                    if (itemStack != null) {
                        getMaterials().giveItemStack(target, itemStack);
                        player.sendMessage(getMessage().get("commands.give", target.getName(), String.valueOf(amount), getMessage().toTitleCase(args[1])));
                        return true;
                    }
                }
            } else if (args.length == 3) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var amount = Integer.parseInt(args[2]);
                    var itemStack = getMaterials().getItemStack(args[1], amount);
                    if (itemStack != null) {
                        getMaterials().giveItemStack(target, itemStack);
                        player.sendMessage(getMessage().get("commands.give", target.getName(), String.valueOf(amount), getMessage().toTitleCase(args[1])));
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var amount = 1;
                    var itemStack = getMaterials().getItemStack(args[1], amount);
                    if (itemStack != null) {
                        getMaterials().giveItemStack(target, itemStack);
                        consoleCommandSender.sendMessage(getMessage().get("commands.give", target.getName(), String.valueOf(amount), getMessage().toTitleCase(args[1])));
                        return true;
                    }
                }
            } else if (args.length == 3) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var amount = Integer.parseInt(args[2]);
                    var itemStack = getMaterials().getItemStack(args[1], amount);
                    if (itemStack != null) {
                        getMaterials().giveItemStack(target, itemStack);
                        consoleCommandSender.sendMessage(getMessage().get("commands.give", target.getName(), String.valueOf(amount), getMessage().toTitleCase(args[1])));
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
        if (sender instanceof Player) {
            if (args.length == 1) {
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata().isVanished(target)) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            } else if (args.length == 2) {
                for (var material : Material.values()) {
                    var materialName = material.name().toLowerCase();
                    if (materialName.startsWith(args[1])) {
                        commands.add(materialName);
                    }
                }
            } else if (args.length == 3) {
                commands.add("8");
                commands.add("16");
                commands.add("32");
                commands.add("64");
            }
        }
        return commands;
    }
}