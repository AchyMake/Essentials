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

public class TPDenyCommand implements CommandExecutor, TabCompleter {
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
    public TPDenyCommand() {
        getInstance().getCommand("tpdeny").setExecutor(this);
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
                    var target = sender.getServer().getPlayer(userdata.getTpaFrom().getUniqueId());
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        var taskID = userdataTarget.getTaskID("tpa");
                        if (getScheduler().isQueued(taskID)) {
                            getScheduler().cancel(taskID);
                            getMessage().send(target, player.getName() + "&6 denied tpa request");
                            getMessage().send(player, "&6You denied tpa request");
                            userdata.setString("tpa.from", null);
                            userdataTarget.setString("tpa.sent", null);
                            userdataTarget.disableTask("tpa");
                        }
                    }
                } else if (userdata.getTpaHereFrom() != null) {
                    var target = sender.getServer().getPlayer(userdata.getTpaHereFrom().getUniqueId());
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        var taskID = userdataTarget.getTaskID("tpahere");
                        if (getScheduler().isQueued(taskID)) {
                            getScheduler().cancel(taskID);
                            getMessage().send(target, player.getName() + "&6 denied tpahere request");
                            getMessage().send(player, "&6You denied tpahere request");
                            userdata.setString("tpahere.from", null);
                            userdataTarget.setString("tpahere.sent", null);
                            userdataTarget.disableTask("tpahere");
                        }
                    }
                } else getMessage().send(player, "&cYou do not have any tp request");
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