package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.CooldownHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RepairCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private CooldownHandler getCooldown() {
        return getInstance().getCooldownHandler();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public RepairCommand() {
        getInstance().getCommand("repair").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                int timer = getInstance().getConfig().getInt("commands.cooldown.repair");
                var heldItem = player.getInventory().getItemInMainHand();
                if (!getMaterials().isAir(heldItem)) {
                    if (!getCooldown().has(player, "repair", timer)) {
                        if (getMaterials().repair(heldItem)) {
                            getCooldown().add(player, "repair", timer);
                            player.sendMessage(getMessage().get("commands.repair.damaged", getMessage().toTitleCase(heldItem.getType().toString())));
                        } else player.sendMessage(getMessage().get("commands.repair.non-damaged", getMessage().toTitleCase(heldItem.getType().toString())));
                    } else player.sendMessage(getMessage().get("commands.repair.cooldown", getCooldown().get(player, "repair", timer)));
                } else player.sendMessage(getMessage().get("error.item.invalid"));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("force")) {
                    if (player.hasPermission("essentials.command.repair.force")) {
                        var heldItem = player.getInventory().getItemInMainHand();
                        if (!getMaterials().isAir(heldItem)) {
                            if (getMaterials().repair(heldItem)) {
                                player.sendMessage(getMessage().get("commands.repair.damaged", getMessage().toTitleCase(heldItem.getType().toString())));
                            } else player.sendMessage(getMessage().get("commands.repair.non-damaged", getMessage().toTitleCase(heldItem.getType().toString())));
                        } else player.sendMessage(getMessage().get("error.item.invalid"));
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
                if (player.hasPermission("essentials.command.repair.force")) {
                    commands.add("force");
                }
            }
        }
        return commands;
    }
}