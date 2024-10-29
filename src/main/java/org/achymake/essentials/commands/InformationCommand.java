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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InformationCommand implements CommandExecutor, TabCompleter {
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
    public InformationCommand() {
        getInstance().getCommand("information").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else {
                if (args.length == 1) {
                    var offlinePlayer = player.getServer().getOfflinePlayer(args[0]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        var simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        getMessage().send(player, "&m&7----------------------");
                        getMessage().send(player, "&6name:&f " + userdataOffline.getName());
                        getMessage().send(player, "&6account:&f " + getEconomy().currency() + getEconomy().format(userdataOffline.getAccount()));
                        getMessage().send(player, "&6bank:&f " + getEconomy().currency() + getEconomy().format(userdataOffline.getBankAccount()));
                        getMessage().send(player, "&6homes:&f " + userdataOffline.getHomes().size());
                        if (!userdataOffline.getHomes().isEmpty()) {
                            userdataOffline.getHomes().forEach(home -> getMessage().send(player, "- " + home));
                        }
                        getMessage().send(player, "&6muted:&f " + userdataOffline.isMuted());
                        getMessage().send(player, "&6frozen:&f " + userdataOffline.isFrozen());
                        getMessage().send(player, "&6jailed:&f " + userdataOffline.isJailed());
                        getMessage().send(player, "&6pvp:&f " + userdataOffline.isPVP());
                        getMessage().send(player, "&6banned:&f " + userdataOffline.isBanned());
                        getMessage().send(player, "&6ban-reason:&f " + userdataOffline.getBanReason());
                        getMessage().send(player, "&6ban-expire:&f " + simpleDateFormat.format(userdataOffline.getBanExpire()));
                        getMessage().send(player, "&6vanished:&f " + userdataOffline.isVanished());
                        getMessage().send(player, "&6last online:&f " + simpleDateFormat.format(offlinePlayer.getLastPlayed()));
                        var quit = userdataOffline.getLocation("quit");
                        if (quit != null) {
                            getMessage().send(player, "&6quit location:&f " + quit.getWorld().getName() + ", &6X:&f" + quit.getBlockX() + ", &6Y:&f" + quit.getBlockY() + ", &6Z:&f" + quit.getBlockZ());
                        }
                        getMessage().send(player, "&6uuid:&f " + userdataOffline.getUUID());
                        getMessage().send(player, "&m&7----------------------");
                    } else getMessage().send(player, userdataOffline.getName() + "&c has never joined");
                    return true;
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
            }
        }
        return commands;
    }
}