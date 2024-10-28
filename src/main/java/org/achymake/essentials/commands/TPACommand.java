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

public class TPACommand implements CommandExecutor, TabCompleter {
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
    public TPACommand() {
        getInstance().getCommand("tpa").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 1) {
                var target = player.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (target != player) {
                        if (userdata.getTpaSent() == null) {
                            var userdataTarget = getUserdata(target);
                            var taskID = getScheduler().runLater(new Runnable() {
                                @Override
                                public void run() {
                                    userdataTarget.setString("tpa.from", null);
                                    userdata.setString("tpa.sent", null);
                                    userdata.disableTask("tpa");
                                    getMessage().send(player, "&cTeleport request has expired");
                                    getMessage().send(target, "&cTeleport request has expired");
                                }
                            }, 300).getTaskId();
                            userdata.setString("tpa.sent", target.getUniqueId().toString());
                            userdataTarget.setString("tpa.from", player.getUniqueId().toString());
                            userdata.addTaskID("tpa", taskID);
                            getMessage().send(target, player.getName() + "&6 has sent you a tpa request");
                            getMessage().send(target, "&6You can type&a /tpaccept&6 or&c /tpdeny");
                            getMessage().send(player, "&6You have sent a tpa request to&f " + target.getName());
                            getMessage().send(player, "&6You can type&c /tpcancel");
                        } else {
                            getMessage().send(player, "&cYou already sent tp request");
                            getMessage().send(player, "&cYou can type&f /tpcancel");
                        }
                    } else getMessage().send(player, "&cYou can't send request to your self");
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