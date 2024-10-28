package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
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
                    OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    var userdata = getUserdata(offlinePlayer);
                    if (userdata.exists()) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        getMessage().send(player, "&m&7----------------------");
                        getMessage().send(player, "&6name:&f " + userdata.getName());
                        getMessage().send(player, "&6uuid:&f " + userdata.getUUID());
                        getMessage().send(player, "&6last online:&f " + simpleDateFormat.format(offlinePlayer.getLastPlayed()));
                        getMessage().send(player, "&6homes:&f " + userdata.getHomes().size());
                        getMessage().send(player, "&6muted:&f " + userdata.isMuted());
                        getMessage().send(player, "&6frozen:&f " + userdata.isFrozen());
                        getMessage().send(player, "&6jailed:&f " + userdata.isJailed());
                        getMessage().send(player, "&6pvp:&f " + userdata.isPVP());
                        getMessage().send(player, "&6banned:&f " + userdata.isBanned());
                        getMessage().send(player, "&6ban-reason:&f " + userdata.getBanReason());
                        getMessage().send(player, "&6ban-expire:&f " + simpleDateFormat.format(userdata.getBanExpire()));
                        getMessage().send(player, "&6vanished:&f " + userdata.isVanished());
                        getMessage().send(player, "&m&7----------------------");
                    } else getMessage().send(player, userdata.getName() + "&c has never joined");
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