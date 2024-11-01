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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EnchantCommand implements CommandExecutor, TabCompleter {
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
                    var enchantment = Enchantment.getByName(args[0].toUpperCase());
                    if (enchantment != null) {
                        var enchantName = getMessage().toTitleCase(enchantment.getKey().getKey());
                        if (itemMeta.hasEnchant(enchantment)) {
                            heldItem.removeEnchantment(enchantment);
                            player.sendMessage(getMessage().get("commands.enchant.remove", enchantName));
                        } else {
                            heldItem.addUnsafeEnchantment(enchantment, 1);
                            player.sendMessage(getMessage().get("commands.enchant.add", enchantName, String.valueOf(1)));
                        }
                    }
                } else player.sendMessage(getMessage().get("commands.enchant.air"));
                return true;
            } else if (args.length == 2) {
                var heldItem = player.getInventory().getItemInMainHand();
                if (!getMaterials().isAir(heldItem)) {
                    var enchantment = Enchantment.getByName(args[0].toUpperCase());
                    if (enchantment != null) {
                        var enchantName = getMessage().toTitleCase(enchantment.getKey().getKey());
                        var amount = Integer.parseInt(args[1]);
                        if (amount > 0) {
                            heldItem.addUnsafeEnchantment(enchantment, amount);
                            player.sendMessage(getMessage().get("commands.enchant.add", enchantName, String.valueOf(amount)));
                        } else {
                            heldItem.removeEnchantment(enchantment);
                            player.sendMessage(getMessage().get("commands.enchant.remove", enchantName));
                        }
                    }
                } else player.sendMessage(getMessage().get("commands.enchant.air"));
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
                for (var enchantment : Enchantment.values()) {
                    var enchantName = enchantment.getKey().getKey();
                    if (enchantName.startsWith(args[0])) {
                        commands.add(enchantName);
                    }
                }
            } else if (args.length == 2) {
                commands.add(String.valueOf(Enchantment.getByName(args[0].toUpperCase()).getMaxLevel()));
            }
        }
        return commands;
    }
}