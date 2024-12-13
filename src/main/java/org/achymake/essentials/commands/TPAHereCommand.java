package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TPAHereCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    public TPAHereCommand() {
        getInstance().getCommand("tpahere").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var userdata = getUserdata(player);
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target != player) {
                        if (userdata.getTpaHereSent() == null) {
                            var userdataTarget = getUserdata(target);
                            int taskID = getScheduler().runLater( new Runnable() {
                                @Override
                                public void run() {
                                    userdataTarget.setString("tpahere.from", null);
                                    userdata.setString("tpahere.sent", null);
                                    userdata.removeTask("tpahere");
                                    target.sendMessage(getMessage().get("commands.tpahere.expired"));
                                    player.sendMessage(getMessage().get("commands.tpahere.expired"));
                                }
                            }, 300).getTaskId();
                            userdataTarget.setString("tpahere.from", player.getUniqueId().toString());
                            userdata.setString("tpahere.sent", target.getUniqueId().toString());
                            userdata.addTaskID("tpahere", taskID);
                            target.sendMessage(getMessage().get("commands.tpahere.target.notify", player.getName()));
                            target.sendMessage(getMessage().get("commands.tpahere.target.decide"));
                            player.sendMessage(getMessage().get("commands.tpahere.sender.notify", target.getName()));
                            player.sendMessage(getMessage().get("commands.tpahere.sender.decide"));
                        } else {
                            player.sendMessage(getMessage().get("commands.tpahere.occupied"));
                            player.sendMessage(getMessage().get("commands.tpahere.sender.decide"));
                        }
                        return true;
                    } else player.sendMessage(getMessage().get("commands.tpahere.request-self"));
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