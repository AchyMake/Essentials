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

import java.util.ArrayList;
import java.util.List;

public class SetHomeCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SetHomeCommand() {
        getInstance().getCommand("sethome").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (args.length == 0) {
                if (userdata.setHome("home")) {
                    player.sendMessage(getMessage().get("commands.sethome.success", "home"));
                } else player.sendMessage(getMessage().get("commands.sethome.limit-reached", String.valueOf(userdata.getHomes().size())));
                return true;
            } else if (args.length == 1) {
                var homeName = args[0].toLowerCase();
                if (!homeName.equalsIgnoreCase("bed")) {
                    if (userdata.setHome(homeName)) {
                        player.sendMessage(getMessage().get("commands.sethome.success", homeName));
                    } else player.sendMessage(getMessage().get("commands.sethome.limit-reached", String.valueOf(userdata.getHomes().size())));
                } else player.sendMessage(getMessage().get("commands.sethome.bed"));
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                commands.addAll(getUserdata(player).getHomes());
            }
        }
        return commands;
    }
}