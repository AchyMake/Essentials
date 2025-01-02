package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScheduleHandler;
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
    private Userdata getUserdata() {
        return getInstance().getUserdata();
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
                if (!getUserdata().hasTaskID(player, "tpahere")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target != player) {
                            int taskID = getScheduler().runLater( new Runnable() {
                                @Override
                                public void run() {
                                    getUserdata().setString(target, "tpahere.from", null);
                                    getUserdata().setString(player, "tpahere.sent", null);
                                    getUserdata().removeTask(target, "tpahere");
                                    getUserdata().removeTask(player, "tpahere");
                                    target.sendMessage(getMessage().get("commands.tpahere.expired"));
                                    player.sendMessage(getMessage().get("commands.tpahere.expired"));
                                }
                            }, 300).getTaskId();
                            getUserdata().setString(target, "tpahere.from", player.getUniqueId().toString());
                            getUserdata().setString(player, "tpahere.sent", target.getUniqueId().toString());
                            getUserdata().addTaskID(target, "tpahere", taskID);
                            getUserdata().addTaskID(player, "tpahere", taskID);
                            target.sendMessage(getMessage().get("commands.tpahere.target.notify", player.getName()));
                            target.sendMessage(getMessage().get("commands.tpahere.target.decide"));
                            player.sendMessage(getMessage().get("commands.tpahere.sender.notify", target.getName()));
                            player.sendMessage(getMessage().get("commands.tpahere.sender.decide"));
                        } else player.sendMessage(getMessage().get("commands.tpahere.request-self"));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                } else {
                    player.sendMessage(getMessage().get("commands.tpahere.occupied"));
                    player.sendMessage(getMessage().get("commands.tpahere.sender.decide"));
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