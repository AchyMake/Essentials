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
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (target != player) {
                        if (userdata.getTpaHereSent() == null) {
                            var userdataTarget = getUserdata(target);
                            int taskID = getScheduler().runLater( new Runnable() {
                                @Override
                                public void run() {
                                    userdataTarget.setString("tpahere.from", null);
                                    userdata.setString("tpahere.sent", null);
                                    userdata.disableTask("tpahere");
                                    getMessage().send(player, "&cTeleport request has expired");
                                    getMessage().send(target, "&cTeleport request has expired");
                                }
                            }, 300).getTaskId();
                            userdataTarget.setString("tpahere.from", player.getUniqueId().toString());
                            userdata.setString("tpahere.sent", target.getUniqueId().toString());
                            userdata.addTaskID("tpahere", taskID);
                            getMessage().send(target, player.getName() + "&6 has sent you a tpahere request");
                            getMessage().send(target, "&6You can type&a /tpaccept&6 or&c /tpdeny");
                            getMessage().send(player, "&6You have sent a tpahere request to&f " + target.getName());
                            getMessage().send(player, "&6You can type&c /tpcancel");
                        } else {
                            getMessage().send(player, "&cYou already sent tp request");
                            getMessage().send(player, "&cYou can type&f /tpcancel");
                        }
                        return true;
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