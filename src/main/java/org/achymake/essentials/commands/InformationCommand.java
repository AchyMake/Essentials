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
            if (args.length == 1) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                var userdataOffline = getUserdata(offlinePlayer);
                if (userdataOffline.exists()) {
                    var simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    player.sendMessage(getMessage().get("commands.information.title"));
                    player.sendMessage(getMessage().get("commands.information.name", userdataOffline.getName()));
                    player.sendMessage(getMessage().get("commands.information.account", getEconomy().currency()) + getEconomy().format(userdataOffline.getAccount()));
                    player.sendMessage(getMessage().get("commands.information.bank", getEconomy().currency()) + getEconomy().format(userdataOffline.getBankAccount()));
                    player.sendMessage(getMessage().get("commands.information.homes", String.valueOf(userdataOffline.getHomes().size())));
                    if (!userdataOffline.getHomes().isEmpty()) {
                        userdataOffline.getHomes().forEach(home -> player.sendMessage(getMessage().get("commands.information.listed", home)));
                    }
                    player.sendMessage(getMessage().get("commands.information.muted", String.valueOf(userdataOffline.isMuted())));
                    player.sendMessage(getMessage().get("commands.information.frozen", String.valueOf(userdataOffline.isFrozen())));
                    player.sendMessage(getMessage().get("commands.information.jailed", String.valueOf(userdataOffline.isJailed())));
                    player.sendMessage(getMessage().get("commands.information.pvp", String.valueOf(userdataOffline.isPVP())));
                    player.sendMessage(getMessage().get("commands.information.banned", String.valueOf(userdataOffline.isBanned())));
                    player.sendMessage(getMessage().get("commands.information.ban-reason", userdataOffline.getBanReason()));
                    player.sendMessage(getMessage().get("commands.information.ban-expire", simpleDateFormat.format(userdataOffline.getBanExpire())));
                    player.sendMessage(getMessage().get("commands.information.vanished", String.valueOf(userdataOffline.isVanished())));
                    player.sendMessage(getMessage().get("commands.information.last-online", simpleDateFormat.format(offlinePlayer.getLastPlayed())));
                    var quit = userdataOffline.getLocation("quit");
                    if (quit != null) {
                        player.sendMessage(getMessage().get("commands.information.quit-location", quit.getWorld().getName(), String.valueOf(quit.getBlockX()), String.valueOf(quit.getBlockY()), String.valueOf(quit.getBlockZ())));
                    }
                    player.sendMessage(getMessage().get("commands.information.uuid", String.valueOf(userdataOffline.getUUID())));
                } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
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