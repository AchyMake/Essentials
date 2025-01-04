package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WhisperCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public WhisperCommand() {
        getInstance().getCommand("whisper").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 1) {
                var username = args[0];
                var target = getInstance().getPlayer(username);
                if (target != null) {
                    var message = getMessage().toString(args, 1);
                    getUserdata().setString(target, "last-whisper", player.getUniqueId().toString());
                    getUserdata().setString(player, "last-whisper", target.getUniqueId().toString());
                    target.sendMessage(getMessage().get("commands.whisper.target", player.getName(), message));
                    player.sendMessage(getMessage().get("commands.whisper.sender", target.getName(), message));
                    getMessage().sendAll(getMessage().get("commands.whisper.notify", player.getName(), target.getName(), message), "essentials.command.whisper.notify");
                } else player.sendMessage(getMessage().get("error.target.offline", username));
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
                    if (!getUserdata().isVanished(target)) {
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