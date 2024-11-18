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
            if (args.length > 0) {
                var userdata = getUserdata(player);
                if (userdata.getLastWhisper() != null) {
                    var target = userdata.getLastWhisper().getPlayer();
                    if (target != null) {
                        var message = getMessage().getBuilder(args, 0);
                        target.sendMessage(getMessage().get("commands.respond.target", player.getName(), message));
                        player.sendMessage(getMessage().get("commands.respond.sender", target.getName(), message));
                        userdata.setString("last-whisper", target.getUniqueId().toString());
                        getUserdata(target).setString("last-whisper", player.getUniqueId().toString());
                        getMessage().sendAll(getMessage().get("commands.respond.notify", player.getName(), target.getName(), message), "essentials.command.whisper.notify");
                    } else player.sendMessage(getMessage().get("error.target.offline", userdata.getLastWhisper().getName()));
                } else player.sendMessage(getMessage().get("commands.respond.invalid"));
                return true;
            }
        }
        return false;
    }
    @Override
    public List onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.EMPTY_LIST;
    }
}