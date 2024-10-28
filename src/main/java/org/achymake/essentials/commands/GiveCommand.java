package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand implements CommandExecutor, TabCompleter {
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
    public GiveCommand() {
        getInstance().getCommand("give").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 2) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    var type = args[1];
                    var itemStack = getMaterials().getItem(type, 1);
                    if (itemStack != null) {
                        getMaterials().giveItem(target, itemStack);
                        var itemName = getMessage().toTitleCase(itemStack.getType().toString());
                        getMessage().send(player, "&6You gave&f " + target.getName() + " &61&f " + itemName);
                        return true;
                    }
                }
            } else if (args.length == 3) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    var type = args[1];
                    var amount = Integer.parseInt(args[2]);
                    var itemStack = getMaterials().getItem(type, amount);
                    if (itemStack != null) {
                        getMaterials().giveItem(target, itemStack);
                        var itemName = getMessage().toTitleCase(itemStack.getType().toString());
                        getMessage().send(player, "&6You gave&f " + target.getName() + " &6" + amount + "&f " + itemName);
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
                    if (!getUserdata(target).isVanished()) {
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