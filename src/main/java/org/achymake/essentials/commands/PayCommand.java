package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public PayCommand() {
        getInstance().getCommand("pay").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 2) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != player) {
                    if (target != null) {
                        if (getUserdata(target).exists()) {
                            var amount = Double.parseDouble(args[1]);
                            if (amount >= getEconomy().getMinimumPayment()) {
                                if (getEconomy().has(player, amount)) {
                                    getEconomy().remove(player, amount);
                                    getEconomy().add(target, amount);
                                    getMessage().send(player, "&6You paid&f " + target.getName() + "&a " + getEconomy().currency() + getEconomy().format(amount));
                                    getMessage().send(target, "&6You received&a " + getEconomy().currency() + getEconomy().format(amount) + "&6 from&f " + player.getName());
                                } else getMessage().send(player, "&cYou don't have&a " + getEconomy().currency() + getEconomy().format(amount) + "&c to pay&f " + target.getName());
                            } else getMessage().send(player, "&cYou have to pay at least&a " + getEconomy().currency() + getEconomy().format(getEconomy().getMinimumPayment()));
                        } else getMessage().send(player, target.getName() + "&c has never joined");
                    } else {
                        var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                        if (getUserdata(offlinePlayer).exists()) {
                            var amount = Double.parseDouble(args[1]);
                            if (amount >= getEconomy().getMinimumPayment()) {
                                if (getEconomy().has(player, amount)) {
                                    getEconomy().remove(player, amount);
                                    getEconomy().add(offlinePlayer, amount);
                                    getMessage().send(player, "&6You paid&f " + offlinePlayer.getName() + "&a " + getEconomy().currency() + getEconomy().format(amount));
                                } else getMessage().send(player, "&cYou don't have&a " + getEconomy().currency() + getEconomy().format(amount) + "&c to pay&f " + offlinePlayer.getName());
                            } else getMessage().send(player, "&cYou have to pay at least&a " + getEconomy().currency() + getEconomy().format(getEconomy().getMinimumPayment()));
                        } else getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                } else getMessage().send(player, "&cYou cannot pay your self");
                return true;
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
            }
        }
        return commands;
    }
}