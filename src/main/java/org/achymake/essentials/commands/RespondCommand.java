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

import java.util.Collections;
import java.util.List;

public class RespondCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public RespondCommand() {
        getInstance().getCommand("respond").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length > 0) {
                if (userdata.getConfig().isString("last-whisper")) {
                    if (userdata.getLastWhisper() != null) {
                        var target = player.getServer().getPlayer(userdata.getLastWhisper().getUniqueId());
                        if (target != null) {
                            var userdataTarget = getUserdata(target);
                            var senderName = player.getName();
                            var targetName = target.getName();
                            var message = getMessage().getBuilder(args, 1);
                            getMessage().send(player, "&7You > " + targetName + ": " + message);
                            getMessage().send(target, "&7" + senderName + " > You: " + message);
                            getMessage().sendAll("&7" + senderName + " > " + targetName + ": " + message, "essentials.command.whisper.notify");
                            userdata.setString("last-whisper", target.getUniqueId().toString());
                            userdataTarget.setString("last-whisper", player.getUniqueId().toString());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    @Override
    public List onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.EMPTY_LIST;
    }
}