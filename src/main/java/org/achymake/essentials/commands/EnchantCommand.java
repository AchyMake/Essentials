package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EnchantCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public EnchantCommand() {
        getInstance().getCommand("enchant").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var heldItem = player.getInventory().getItemInMainHand();
                if (!getMaterials().isAir(heldItem)) {
                    var itemMeta = heldItem.getItemMeta();
                    var enchantName = args[0];
                    if (getMaterials().isEnchantment(enchantName)) {
                        if (itemMeta.hasEnchant(getMaterials().getEnchantment(enchantName))) {
                            getMaterials().setEnchantment(heldItem, enchantName, 0);
                            player.sendMessage(getMessage().get("commands.enchant.remove", getMessage().toTitleCase(enchantName)));
                        } else {
                            getMaterials().setEnchantment(heldItem, enchantName, 1);
                            player.sendMessage(getMessage().get("commands.enchant.add", getMessage().toTitleCase(enchantName), String.valueOf(1)));
                        }
                    } else player.sendMessage(getMessage().get("error.enchantment.invalid", enchantName));
                } else player.sendMessage(getMessage().get("error.item.invalid"));
                return true;
            } else if (args.length == 2) {
                var heldItem = player.getInventory().getItemInMainHand();
                if (!getMaterials().isAir(heldItem)) {
                    var enchantName = args[0];
                    var amount = getMessage().getInteger(args[1]);
                    getMaterials().setEnchantment(heldItem, enchantName, amount);
                    if (amount > 0) {
                        player.sendMessage(getMessage().get("commands.enchant.add", getMessage().toTitleCase(enchantName), String.valueOf(amount)));
                    } else player.sendMessage(getMessage().get("commands.enchant.remove", getMessage().toTitleCase(enchantName)));
                } else player.sendMessage(getMessage().get("error.item.invalid"));
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                getMaterials().getEnchantments().forEach(enchantment -> {
                    var enchantName = enchantment.getKey().getKey();
                    if (enchantName.startsWith(args[0])) {
                        commands.add(enchantName);
                    }
                });
            } else if (args.length == 2) {
                commands.add(String.valueOf(getMaterials().getEnchantment(args[0]).getMaxLevel()));
            }
        }
        return commands;
    }
}