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

public class TPACommand implements CommandExecutor, TabCompleter {
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
    public TPACommand() {
        getInstance().getCommand("tpa").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target != player) {
                        if (getUserdata().getTpaSent(player) == null) {
                            var taskID = getScheduler().runLater(new Runnable() {
                                @Override
                                public void run() {
                                    getUserdata().setString(target, "tpa.from", null);
                                    getUserdata().setString(player, "tpa.sent", null);
                                    getUserdata().removeTask(player, "tpa");
                                    target.sendMessage(getMessage().get("commands.tpa.expired"));
                                    player.sendMessage(getMessage().get("commands.tpa.expired"));
                                }
                            }, 300).getTaskId();
                            getUserdata().setString(player, "tpa.sent", target.getUniqueId().toString());
                            getUserdata().setString(target, "tpa.from", player.getUniqueId().toString());
                            getUserdata().addTaskID(player, "tpa", taskID);
                            target.sendMessage(getMessage().get("commands.tpa.target.notify", player.getName()));
                            target.sendMessage(getMessage().get("commands.tpa.target.decide"));
                            player.sendMessage(getMessage().get("commands.tpa.sender.notify", target.getName()));
                            player.sendMessage(getMessage().get("commands.tpa.sender.decide"));
                        } else {
                            player.sendMessage(getMessage().get("commands.tpa.occupied"));
                            player.sendMessage(getMessage().get("commands.tpa.sender.decide"));
                        }
                    } else player.sendMessage(getMessage().get("commands.tpa.request-self"));
                } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
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