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

import java.util.Collections;
import java.util.List;

public class TPAcceptCommand implements CommandExecutor, TabCompleter {
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
    public TPAcceptCommand() {
        getInstance().getCommand("tpaccept").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                if (userdata.getTpaFrom() != null) {
                    var target = player.getServer().getPlayer(userdata.getTpaFrom().getUniqueId());
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        var taskID = userdata.getTaskID("tpa");
                        if (getScheduler().isQueued(taskID)) {
                            getMessage().send(player, "&6You accepted&f " + target.getName() + "&6 tpa request");
                            getMessage().send(target, player.getName() + "&6 accepted tpa request");
                            getMessage().sendActionBar(target, "&6Teleporting to&f " + player.getName());
                            target.teleport(player);
                            userdataTarget.setString("tpa.sent", null);
                            userdata.setString("tpa.from", null);
                            userdataTarget.disableTask("tpa");
                        }
                    }
                } else if (userdata.getTpaHereFrom() != null) {
                    var target = player.getServer().getPlayer(userdata.getTpaHereFrom().getUniqueId());
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        var taskID = userdataTarget.getTaskID("tpahere");
                        if (getScheduler().isQueued(taskID)) {
                            getMessage().send(target, player.getName() + "&6 accepted tpahere request");
                            getMessage().send(player, "&6You accepted&f " + target.getName() + "&6 tpahere request");
                            getMessage().sendActionBar(player, "&6Teleporting to&f " + player.getName());
                            player.teleport(target);
                            userdataTarget.setString("tpahere.sent", null);
                            userdata.setString("tpahere.from", null);
                            userdataTarget.disableTask("tpahere");
                        }
                    }
                } else getMessage().send(player, "&cYou don't have any tp request");
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