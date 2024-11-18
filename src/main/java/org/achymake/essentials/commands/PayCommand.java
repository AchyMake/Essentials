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
            if (args.length == 2) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target != player) {
                        var amount = Double.parseDouble(args[1]);
                        if (amount >= getEconomy().getMinimumPayment()) {
                            if (getEconomy().has(player, amount)) {
                                getEconomy().add(target, amount);
                                getEconomy().remove(player, amount);
                                target.sendMessage(getMessage().get("commands.pay.target", getEconomy().currency() + getEconomy().format(amount), player.getName()));
                                player.sendMessage(getMessage().get("commands.pay.sender", target.getName(), getEconomy().currency() + getEconomy().format(amount)));
                            } else player.sendMessage(getMessage().get("commands.pay.insufficient-funds", getEconomy().currency() + getEconomy().format(amount), target.getName()));
                        } else player.sendMessage(getMessage().get("commands.pay.minimum-payment", getEconomy().currency() + getEconomy().format(getEconomy().getMinimumPayment())));
                    } else player.sendMessage(getMessage().get("commands.pay.self"));
                } else {
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    if (getUserdata(offlinePlayer).exists()) {
                        var amount = Double.parseDouble(args[1]);
                        if (amount >= getEconomy().getMinimumPayment()) {
                            if (getEconomy().has(player, amount)) {
                                getEconomy().add(offlinePlayer, amount);
                                getEconomy().remove(player, amount);
                                player.sendMessage(getMessage().get("commands.pay.sender", offlinePlayer.getName(), getEconomy().currency() + getEconomy().format(amount)));
                            } else player.sendMessage(getMessage().get("commands.pay.insufficient-funds", getEconomy().currency() + getEconomy().format(amount), offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("commands.pay.minimum-payment", getEconomy().currency() + getEconomy().format(getEconomy().getMinimumPayment())));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
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